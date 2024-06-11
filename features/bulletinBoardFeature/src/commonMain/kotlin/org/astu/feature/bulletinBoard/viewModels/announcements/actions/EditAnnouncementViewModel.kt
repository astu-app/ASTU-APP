package org.astu.feature.bulletinBoard.viewModels.announcements.actions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.models.AnnouncementModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.EditAnnouncementErrorsAggregate
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetAnnouncementEditContentErrors
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.announcements.EditAnnouncement
import org.astu.feature.bulletinBoard.models.entities.audience.CheckableUser
import org.astu.feature.bulletinBoard.models.entities.common.UpdateIdentifierList
import org.astu.feature.bulletinBoard.viewModels.humanization.ErrorCodesHumanization.humanize
import org.astu.feature.bulletinBoard.views.entities.announcement.editing.EditAnnouncementContent
import org.astu.feature.bulletinBoard.views.entities.attachments.AttachmentToModelMappers.toModel

class EditAnnouncementViewModel(
    private val announcementId: Uuid,
    private val onReturn: () -> Unit
) : StateScreenModel<EditAnnouncementViewModel.State>(State.EditContentLoading) {
    sealed class State {
        data object EditContentLoading : State()
        data object EditingAnnouncement : State()
        data object EditContentLoadingError : State()
        data object ChangesUploading : State()
        data object ChangesUploadingDone : State()
        data object ChangesUploadingError : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    private var original: ContentForAnnouncementEditing? = null
    var content: MutableState<EditAnnouncementContent?> = mutableStateOf(null)

    private val unexpectedErrorTitle: String = "Ошибка"

    val errorDialogLabel: MutableState<String> = mutableStateOf(unexpectedErrorTitle)
    val errorDialogBody: MutableState<String> = mutableStateOf("Ошибка")
    val onErrorDialogTryAgain: MutableState<() -> Unit> = mutableStateOf( { } )
    val onErrorDialogDismiss: MutableState<() -> Unit> = mutableStateOf( { } )
    val showErrorDialog: MutableState<Boolean> = mutableStateOf(false)


    init {
        loadEditContent()
    }

    fun canEdit(): Boolean {
        return model.canEdit(content.value)
    }

    private fun loadEditContent() {
        mutableState.value = State.EditContentLoading
        screenModelScope.launch {
            try {
                val getContent = model.getEditContent(announcementId)
                if (getContent.isContentValid) {
                    original = getContent.content
                    content.value = EditAnnouncementContent(getContent.content!!) // так как not-null заложен в isContentValid
                    mutableState.value = State.EditingAnnouncement
                    return@launch
                }
            } catch (exception: Exception) {
                mutableState.value = State.EditContentLoadingError
                setErrorDialogStateForEditContentLoading()
                Logger.e(exception.message ?: "empty message", exception, "LoadAnnouncementEditContent")
            }
        }
    }

    fun edit() {
        mutableState.value = State.ChangesUploading
        screenModelScope.launch {
            try {
                val editAnnouncementModel = toModel() ?: return@launch

                val error = model.edit(editAnnouncementModel)
                if (error == null) {
                    mutableState.value = State.ChangesUploadingDone
                    return@launch
                }

                setErrorDialogStateForAnnouncementCreating(error)
                mutableState.value = State.ChangesUploadingError

            } catch (exception: Exception) {
                setErrorDialogStateForAnnouncementCreating()
                mutableState.value = State.ChangesUploadingError
                Logger.e(exception.message ?: "empty message", exception, "EditAnnouncement")
            }
        }
    }

    private fun toModel(): EditAnnouncement? {
        val contentSnapshot = content.value
        val originalSnapshot = original

        return if (contentSnapshot == null || originalSnapshot == null) null
        else EditAnnouncement(
            id = announcementId,
            content = getUpdatedContent(contentSnapshot, originalSnapshot),
            users = getUpdatedAudienceIds(contentSnapshot, originalSnapshot),
            delayedPublishingAtChanged = isDelayedPublishingChanged(contentSnapshot, originalSnapshot),
            delayedPublishingAt = getChangedDelayedPublicationMoment(contentSnapshot, originalSnapshot),
            delayedHidingAtChanged = isDelayedHidingChanged(contentSnapshot, originalSnapshot),
            delayedHidingAt = getChangedDelayedHidingMoment(contentSnapshot, originalSnapshot),

            attachmentIdsToRemove = emptySet(),
            newSurvey = contentSnapshot.newSurvey.value?.toModel(),
        )
    }

    private fun getUpdatedContent(content: EditAnnouncementContent, original: ContentForAnnouncementEditing): String? =
        if (original.content != content.text.value) content.text.value else null

    private fun getUpdatedAudienceIds(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ): UpdateIdentifierList? {
        val originalIds = original.audienceHierarchy.allMembers.filter { (it as CheckableUser).isChecked }.map { it.id }
        val updatedIds = content.selectedUserIds

        val idsChanged = originalIds.intersect(updatedIds).isNotEmpty()
        if (!idsChanged)
            return null

        return UpdateIdentifierList(
            toAdd = updatedIds subtract originalIds,
            toRemove = originalIds subtract updatedIds,
        )
    }

    private fun isDelayedPublishingChanged(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ) : Boolean {
        val contentDelayedPublicationEnabled = content.delayedPublicationEnabled.value
        val originalDelayedPublicationEnabled = original.delayedPublishingAt != null

        if (contentDelayedPublicationEnabled == false && originalDelayedPublicationEnabled == false) return false
        if (contentDelayedPublicationEnabled != originalDelayedPublicationEnabled) return true

        // contentDelayedPublicationEnabled == true && originalDelayedPublicationEnabled == true
        return content.delayedPublicationAt == original.delayedPublishingAt
    }

    private fun getChangedDelayedPublicationMoment(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ): LocalDateTime? {
        return if (isDelayedPublishingChanged(content, original)) content.delayedPublicationAt else null
    }

    private fun isDelayedHidingChanged(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ) : Boolean {
        val contentDelayedHidingEnabled = content.delayedHidingEnabled.value
        val originalDelayedHidingEnabled = original.delayedHidingAt != null

        if (contentDelayedHidingEnabled == false && originalDelayedHidingEnabled == false) return false
        if (contentDelayedHidingEnabled != originalDelayedHidingEnabled) return true

        return content.delayedHidingAt == original.delayedHidingAt
    }

    private fun getChangedDelayedHidingMoment(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ): LocalDateTime? {
        return if (isDelayedHidingChanged(content, original)) content.delayedHidingAt else null
    }

    private fun setErrorDialogStateForEditContentLoading(error:  GetAnnouncementEditContentErrors? = null) {
        errorDialogBody.value = error.humanize()
        onErrorDialogTryAgain.value = {
            loadEditContent()
        }
        onErrorDialogDismiss.value = {
            mutableState.value = State.EditContentLoadingError
            onReturn()
        }
    }

    private fun setErrorDialogStateForAnnouncementCreating(error: EditAnnouncementErrorsAggregate? = null) {
        errorDialogBody.value = error.humanize()
        onErrorDialogTryAgain.value = {
            edit()
        }
        onErrorDialogDismiss.value = {
            mutableState.value = State.EditingAnnouncement
        }
    }
}