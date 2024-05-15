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
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.EditAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.EditAnnouncementErrorsAggregate
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetAnnouncementEditContentErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CreateSurveyErrors
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.announcements.EditAnnouncement
import org.astu.feature.bulletinBoard.models.entities.audience.SelectableUser
import org.astu.feature.bulletinBoard.models.entities.common.UpdateIdentifierList
import org.astu.feature.bulletinBoard.views.entities.EditAnnouncementContent
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

    fun edit() {
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
            categories = getUpdatedCategoryIds(contentSnapshot, originalSnapshot),
            users = getUpdatedAudienceIds(contentSnapshot, originalSnapshot),
            delayedPublishingAtChanged = isDelayedPublishingChanged(contentSnapshot, originalSnapshot),
            delayedPublishingAt = getChangedDelayedPublicationMoment(contentSnapshot, originalSnapshot),
            delayedHidingAtChanged = isDelayedHidingChanged(contentSnapshot, originalSnapshot),
            delayedHidingAt = getChangedDelayedHidingMoment(contentSnapshot, originalSnapshot),

            attachmentIdsToRemove = getupdatedAttachmentIds(contentSnapshot, originalSnapshot),
            newSurvey = contentSnapshot.newSurvey.value?.toModel(),
            newFiles = null, // todo прикрутить файлы
        )
    }

    private fun getUpdatedContent(content: EditAnnouncementContent, original: ContentForAnnouncementEditing): String? =
        if (original.content != content.text.value) content.text.value else null

    private fun getUpdatedCategoryIds(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ): UpdateIdentifierList? =
        null // todo прикрутить категории объвлений

    private fun getUpdatedAudienceIds(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ): UpdateIdentifierList? {
        val originalIds = original.audienceHierarchy.allMembers.filter { (it as SelectableUser).isSelected }.map { it.id }
        val updatedIds = content.selectedUserIds

        val idsChanged = originalIds.intersect(updatedIds).isNotEmpty()
        if (!idsChanged)
            return null

        return UpdateIdentifierList(
            toAdd = updatedIds subtract originalIds,
            toRemove = originalIds subtract updatedIds,
        )
    }

    private fun getupdatedAttachmentIds(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ): Set<Uuid>? {
        val originalIds = original.files.map { it.id } + (original.surveys?.map { it.id } ?: emptyList())
        val updatedIds = content.selectedUserIds

        val idsChanged = originalIds.intersect(updatedIds).isNotEmpty()
        if (!idsChanged)
            return null

        val toRemove = originalIds subtract updatedIds
        return if (toRemove.isNotEmpty()) toRemove else null
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

        // contentDelayedHidingEnabled == true && originalDelayedHidingEnabled == true
        return content.delayedHidingAt == original.delayedHidingAt
    }

    private fun getChangedDelayedHidingMoment(
        content: EditAnnouncementContent,
        original: ContentForAnnouncementEditing
    ): LocalDateTime? {
        return if (isDelayedHidingChanged(content, original)) content.delayedHidingAt else null
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

    private fun setErrorDialogStateForAnnouncementCreating(error:  EditAnnouncementErrorsAggregate? = null) {
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

    private fun constructCreateErrorDialogContent(error:  EditAnnouncementErrorsAggregate?): String {
        if (error?.editAnnouncementError != null)
            return when (error.editAnnouncementError) {
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

        if (error?.createSurveyError != null)
            return when (error.createSurveyError) {
                CreateSurveyErrors.CreateSurveyForbidden -> "У вас недостаточно прав для создания опроса"
                CreateSurveyErrors.SurveyContainsQuestionSerialsDuplicates -> "Опрос содержит вопросы с одинаковыми порядковыми номерами"
                CreateSurveyErrors.QuestionContainsAnswersSerialsDuplicates -> "Вопрос(ы) содержит варианты ответов с одинаковыми порядковыми номерами"
                else -> "Непредвиденная ошибка при создании опроса. Повторите попытку"
            }

        if (error?.createFilesError != null)
            return when (error.createFilesError) {
                else -> "Непредвиденная ошибка при создании файлов. Повторите попытку"
            }

        return "Непредвиденная ошибка при изменении объявления. Повторите попытку позднее"
    }
}