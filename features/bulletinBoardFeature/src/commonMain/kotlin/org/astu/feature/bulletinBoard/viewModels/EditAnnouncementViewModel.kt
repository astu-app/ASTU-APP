package org.astu.feature.bulletinBoard.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.models.AnnouncementModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.EditAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetAnnouncementEditContentErrors
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.announcements.EditAnnouncement
import org.astu.feature.bulletinBoard.views.entities.EditAnnouncementContent

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
                    original = getContent.content;
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

    private fun edit() {
        mutableState.value = State.ChangesUploading
        screenModelScope.launch {
            try {
                val editAnnouncementModel = toModel() ?: return@launch

                // todo upload attachments
                val error = model.edit(editAnnouncementModel)
                if (error == null) {
                    mutableState.value = State.ChangesUploadingDone
                    return@launch
                }

                mutableState.value = State.ChangesUploadingError
                setErrorDialogStateForAnnouncementCreating(error)

            } catch (exception: Exception) {
                mutableState.value = State.ChangesUploadingError
                setErrorDialogStateForAnnouncementCreating()
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
            categoryIds = getUpdatedCategoryIds(contentSnapshot, originalSnapshot),
            audienceIds = getUpdatedAudienceIds(contentSnapshot, originalSnapshot),
            attachmentIds = getupdatedAttachmentIds(contentSnapshot, originalSnapshot),
            delayedPublishingAtChanged = isDelayedPublishingChanged(contentSnapshot, originalSnapshot),
            delayedPublishingAt = getChangedDelayedPublicationMoment(contentSnapshot, originalSnapshot),
            delayedHidingAtChanged = isDelayedHidingChanged(contentSnapshot, originalSnapshot),
            delayedHidingAt = getChangedDelayedHidingMoment(contentSnapshot, originalSnapshot),
        )
    }

    private fun getUpdatedContent(content: EditAnnouncementContent, original: ContentForAnnouncementEditing): String? =
        if (original.content != content.text.value) content.text.value else null

    private fun getUpdatedCategoryIds(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ): List<Uuid>? =
        null // todo прикрутить категории объвлений

    private fun getUpdatedAudienceIds(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ): List<Uuid>? {
        val originalIds = original.audienceHierarchy.allMembers
        val changedIds = content.selectedUserIds
        return if (originalIds.intersect(changedIds).isNotEmpty()) changedIds.toList() else null
    }

    private fun getupdatedAttachmentIds(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ): List<Uuid>? {
        return null // todo прикрутить вложения
    }

    private fun isDelayedPublishingChanged(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ) : Boolean {
        return content.delayedPublicationAt != original.delayedPublishingAt
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
        return content.delayedPublicationAt != original.delayedPublishingAt
    }

    private fun getChangedDelayedHidingMoment(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ): LocalDateTime? {
        return if (isDelayedHidingChanged(content, original)) content.delayedPublicationAt else null
    }

    private fun setErrorDialogStateForEditContentLoading(error:  GetAnnouncementEditContentErrors? = null) {
        errorDialogBody.value = constructEditContentLoadingErrorDialogContent(error)
        onErrorDialogTryAgain.value = {
            loadEditContent()
            showErrorDialog.value = false
        }
        onErrorDialogDismiss.value = {
            onReturn()
            showErrorDialog.value = false
        }
    }

    private fun constructEditContentLoadingErrorDialogContent(error: GetAnnouncementEditContentErrors?): String {
        return when (error) {
            GetAnnouncementEditContentErrors.GetAnnouncementUpdateContentForbidden -> "У вас недостаточно прав для загрузки данных для редактирования объявления"
            GetAnnouncementEditContentErrors.AnnouncementDoesNotExist -> "Объявление не найдено. Повторите попытку позднее"
            else -> "Непредвиденная ошибка при загрузке данных для редактирования объявления. Повторите попытку позднее"
        }
    }

    private fun setErrorDialogStateForAnnouncementCreating(error:  EditAnnouncementErrors? = null) {
        errorDialogBody.value = constructCreateErrorDialogContent(error)
        onErrorDialogTryAgain.value = {
            edit()
            showErrorDialog.value = false
        }
        onErrorDialogDismiss.value = {
            mutableState.value = State.EditingAnnouncement
            showErrorDialog.value = false
        }
    }

    private fun constructCreateErrorDialogContent(error:  EditAnnouncementErrors?): String {
        return when (error) {
            EditAnnouncementErrors.ContentEmpty -> "Текст объявления не должен быть пустым"
            EditAnnouncementErrors.AudienceEmpty -> "Нельзя очистить аудиторию объявления"
            EditAnnouncementErrors.AnnouncementEditingForbidden -> "У вас недостаточно прав для изменения этого объявления"
            EditAnnouncementErrors.AnnouncementDoesNotExist -> "Объявление не найдено. Повторите попытку позднее"
            EditAnnouncementErrors.AnnouncementCategoriesDoesNotExist -> "Категория объявлений не найдена. Повторите попытку позднее"
            EditAnnouncementErrors.AttachmentsDoNotExist -> "Вложение не найдено. Повторите попытку позднее"
            EditAnnouncementErrors.PieceOfAudienceDoesNotExist -> "Пользователь не найден. Повторите попытку позднее"
            EditAnnouncementErrors.DelayedPublishingMomentIsInPast -> "Момент отложенной публикации уже наступил в прошлом"
            EditAnnouncementErrors.DelayedHidingMomentIsInPast -> "Момент отложенного сокрытия уже наступил в прошлом"
            EditAnnouncementErrors.AutoHidingAnAlreadyHiddenAnnouncement -> "Нельзя задать момент отложенного сокрытия объявлению, которое уже было скрыто"
            EditAnnouncementErrors.AutoPublishingPublishedAndNonHiddenAnnouncement -> "Нельзя задать момент автоматической публикации объявлению, которое уже было опубликовано"
            EditAnnouncementErrors.CannotDetachSurvey -> "Нельзя открепить опрос"
            else -> "Непредвиденная ошибка при изменении объявления. Повторите попытку позднее"
        }
    }
}