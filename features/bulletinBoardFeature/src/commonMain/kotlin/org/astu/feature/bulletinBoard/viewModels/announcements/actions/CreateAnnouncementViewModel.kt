package org.astu.feature.bulletinBoard.viewModels.announcements.actions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.common.utils.localDateTimeFromComponents
import org.astu.feature.bulletinBoard.models.AnnouncementModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.CreateAnnouncementErrorsAggregate
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyErrors
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.feature.bulletinBoard.viewModels.humanization.ErrorCodesHumanization.humanize
import org.astu.feature.bulletinBoard.views.entities.announcement.creation.CreateAnnouncementContent
import org.astu.feature.bulletinBoard.views.entities.attachments.AttachmentToModelMappers.toModel

class CreateAnnouncementViewModel(private val onReturn: () -> Unit) : StateScreenModel<CreateAnnouncementViewModel.State>(
    State.CreateContentLoading
) {
    sealed class State {
        data object CreateContentLoading : State()
        data object CreatingAnnouncement : State()
        data object CreateContentLoadError : State()
        data object NewAnnouncementUploading : State()
        data object NewAnnouncementUploadingDone : State()
        data object NewAnnouncementUploadError : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    val content: MutableState<CreateAnnouncementContent?> = mutableStateOf(null)

    private val unexpectedErrorTitle: String = "Ошибка"

    val errorDialogLabel: MutableState<String> = mutableStateOf(unexpectedErrorTitle)
    val errorDialogBody: MutableState<String> = mutableStateOf("Ошибка")
    val onErrorDialogTryAgain: MutableState<() -> Unit> = mutableStateOf( { } )
    val onErrorDialogDismiss: MutableState<() -> Unit> = mutableStateOf( { } )
    val showErrorDialog: MutableState<Boolean> = mutableStateOf(false)

    init {
        loadCreateContent()
    }

    private fun loadCreateContent() {
        mutableState.value = State.CreateContentLoading
        screenModelScope.launch {
            try {
                val getResult = model.getAudienceHierarchy()
                if (getResult.isContentValid) {
                    content.value = CreateAnnouncementContent(getResult.content!!) // так как not-null заложен в isContentValid
                    mutableState.value = State.CreatingAnnouncement
                    return@launch
                }

                mutableState.value = State.CreateContentLoadError
                setErrorDialogStateForCreateContentLoading(getResult.error)

            } catch (exception: Exception) {
                mutableState.value = State.CreateContentLoadError
                setErrorDialogStateForCreateContentLoading()
                Logger.e(exception.message ?: "empty message", exception, "LoadAnnouncementCreateContent")
            }
        }
    }

    fun canCreate(): Boolean {
        return model.canCreate(content.value)
    }

    fun create() {
        mutableState.value = State.NewAnnouncementUploading
        screenModelScope.launch {
            try {
                val createAnnouncementModel = toModel() ?: return@launch

                val error = model.create(createAnnouncementModel)
                if (error == null) {
                    mutableState.value = State.NewAnnouncementUploadingDone
                    return@launch
                }

                mutableState.value = State.CreateContentLoadError
                setErrorDialogStateForAnnouncementCreating(error)

            } catch (exception: Exception) {
                mutableState.value = State.CreateContentLoadError
                setErrorDialogStateForAnnouncementCreating()
                Logger.e(exception.message ?: "empty message", exception, "CreateAnnouncement")
            }
        }
    }

    private fun toModel(): CreateAnnouncement? {
        val contentSnapshot = content.value

        return if (contentSnapshot == null) null
        else CreateAnnouncement(
            content = contentSnapshot.textContent.value,
            userIds = getUserIds(),
            delayedPublishingAt = getDelayedPublicationMoment(),
            delayedHidingAt = getDelayedHidingMoment(),
            survey = contentSnapshot.survey.value?.toModel(),
        )
    }

    private fun getUserIds(): List<String> {
        val contentSnapshot = content.value ?: return emptyList()

        return contentSnapshot.selectedUserIds.map { it.toString() }
    }

    private fun getDelayedPublicationMoment(): LocalDateTime? {
        val contentSnapshot = content.value ?: return null

        if (!contentSnapshot.delayedPublicationEnabled.value)
            return null

        val dateMillis = contentSnapshot.delayedPublicationDateMillis.value
        val hour = contentSnapshot.delayedPublicationTimeHours.value
        val minute = contentSnapshot.delayedPublicationTimeMinutes.value

        return localDateTimeFromComponents(dateMillis, hour, minute)
    }

    private fun getDelayedHidingMoment(): LocalDateTime? {
        val contentSnapshot = content.value ?: return null

        if (!contentSnapshot.delayedHidingEnabled.value)
            return null

        val dateMillis = contentSnapshot.delayedHidingDateMillis.value
        val hour = contentSnapshot.delayedHidingTimeHours.value
        val minute = contentSnapshot.delayedHidingTimeMinutes.value

        return localDateTimeFromComponents(dateMillis, hour, minute)
    }

    private fun setErrorDialogStateForCreateContentLoading(error: GetUserHierarchyErrors? = null) {
        errorDialogBody.value = error.humanize()
        onErrorDialogTryAgain.value = {
            loadCreateContent()
            showErrorDialog.value = false
        }
        onErrorDialogDismiss.value = {
            onReturn()
            showErrorDialog.value = false
        }
    }

    private fun setErrorDialogStateForAnnouncementCreating(error: CreateAnnouncementErrorsAggregate? = null) {
        errorDialogBody.value = error.humanize()
        onErrorDialogTryAgain.value = {
            create()
            showErrorDialog.value = false
        }
        onErrorDialogDismiss.value = {
            mutableState.value = State.CreatingAnnouncement
            showErrorDialog.value = false
        }
    }
}