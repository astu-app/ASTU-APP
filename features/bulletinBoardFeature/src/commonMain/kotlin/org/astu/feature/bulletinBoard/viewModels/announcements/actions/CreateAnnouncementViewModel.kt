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
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.CreateAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.CreateAnnouncementErrorsAggregate
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CreateSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyErrors
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
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
    val uploadedAttachments: MutableList<String> = mutableListOf()

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
            categoryIds = getCategoryIds(),
            survey = contentSnapshot.survey.value?.toModel(),
            files = null // todo прикрутить файлы
        )
    }

    private fun getUserIds(): List<String> {
        val contentSnapshot = content.value ?: return emptyList()

        return contentSnapshot.selectedUserIds.map { it.toString() }
    }

    private fun getCategoryIds(): List<String> {
        // todo прикрутить категории объявлений
        return emptyList()
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
        errorDialogBody.value = constructCreateContentLoadingErrorDialogContent(error)
        onErrorDialogTryAgain.value = {
            loadCreateContent()
            showErrorDialog.value = false
        }
        onErrorDialogDismiss.value = {
            onReturn()
            showErrorDialog.value = false
        }
    }

    private fun constructCreateContentLoadingErrorDialogContent(error: GetUserHierarchyErrors?): String {
        return when (error) {
            GetUserHierarchyErrors.GetUsergroupHierarchyForbidden -> "У вас недостаточно прав для загрузки аудитории объявления"
            else -> "Непредвиденная ошибка при загрузке контента для создания объявления. Повторите попытку"
        }
    }

    private fun setErrorDialogStateForAnnouncementCreating(error: CreateAnnouncementErrorsAggregate? = null) {
        errorDialogBody.value = constructCreateErrorDialogContent(error)
        onErrorDialogTryAgain.value = {
            create()
            showErrorDialog.value = false
        }
        onErrorDialogDismiss.value = {
            mutableState.value = State.CreatingAnnouncement
            showErrorDialog.value = false
        }
    }

    private fun constructCreateErrorDialogContent(error: CreateAnnouncementErrorsAggregate?): String {
        if (error?.createAnnouncementError != null)
            return when (error.createAnnouncementError) {
                CreateAnnouncementErrors.AudienceNullOrEmpty -> "Аудитория объявления не задана"
                CreateAnnouncementErrors.ContentNullOrEmpty -> "Текст объявления не задан"
                CreateAnnouncementErrors.AnnouncementCreationForbidden -> "У вас недостаточно прав для создания объявлений"
                CreateAnnouncementErrors.AnnouncementCategoriesDoNotExist -> "Не удалось прикрепить одно или несколько категория объявлений"
                CreateAnnouncementErrors.AttachmentsDoNotExist -> "Не удалось прикрепить одно или несколько вложений"
                CreateAnnouncementErrors.PieceOfAudienceDoesNotExist -> "Не удалось прикрепить одного или нескольких из указанных пользователей"
                CreateAnnouncementErrors.DelayedPublishingMomentIsInPast -> "Момент отложенной публикации объявления не может наступить в прошлом"
                CreateAnnouncementErrors.DelayedHidingMomentIsInPast -> "Момент отложенного сокрытия объявления не может наступить в прошлом"
                CreateAnnouncementErrors.DelayedPublishingMomentAfterDelayedHidingMoment -> "Момент отложенной публикации объявления не может наступить после момента отложенного сокрытия"
                else -> "Непредвиденная ошибка при создании объявления. Повторите попытку"
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

        return "Непредвиденная ошибка при создании объявления. Повторите попытку"
    }
}