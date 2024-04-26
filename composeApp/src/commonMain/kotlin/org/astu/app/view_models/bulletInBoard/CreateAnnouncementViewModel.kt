package org.astu.app.view_models.bulletInBoard

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.astu.app.dataSources.bulletInBoard.announcements.common.responses.CreateAnnouncementErrors
import org.astu.app.dataSources.bulletInBoard.userGroups.common.responses.GetUserHierarchyResponses
import org.astu.app.entities.bulletInBoard.announcement.creation.CreateAnnouncementContent
import org.astu.app.models.bulletInBoard.AnnouncementModel
import org.astu.app.models.bulletInBoard.entities.announcements.CreateAnnouncement

class CreateAnnouncementViewModel(private val onReturn: () -> Unit) : StateScreenModel<CreateAnnouncementViewModel.State>(State.CreateContentLoading) {
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
                val getResult = model.getCreateContent()
                if (getResult.isContentValid) {
                    content.value = getResult.content // так как not-null заложен в isContentValid
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

                // todo upload attachments
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
            attachmentIds = uploadedAttachments,
            delayedPublishingAt = getDelayedPublicationMoment(),
            delayedHidingAt = getDelayedHidingMoment(),
            categoryIds = getCategoryIds()
        )
    }

    private fun getUserIds(): List<String> {
        val contentSnapshot = content.value ?: return emptyList()

        return contentSnapshot.selectedUserIds.map { it.toString() }
    }

    private fun getCategoryIds(): List<String> {
        // todo
        return emptyList()
    }

    private fun getDelayedPublicationMoment(): LocalDateTime? {
        val contentSnapshot = content.value ?: return null

        if (!contentSnapshot.delayedPublicationEnabled.value)
            return null

        val dateMillis = contentSnapshot.delayedPublicationDateMillis.value
        val hour = contentSnapshot.delayedPublicationTimeHours.value
        val minute = contentSnapshot.delayedPublicationTimeMinutes.value

        return getDelayedMoment(dateMillis, hour, minute)
    }

    private fun getDelayedHidingMoment(): LocalDateTime? {
        val contentSnapshot = content.value ?: return null

        if (!contentSnapshot.delayedHidingEnabled.value)
            return null

        val dateMillis = contentSnapshot.delayedHidingDateMillis.value
        val hour = contentSnapshot.delayedHidingTimeHours.value
        val minute = contentSnapshot.delayedHidingTimeMinutes.value

        return getDelayedMoment(dateMillis, hour, minute)
    }

    private fun getDelayedMoment(dateMillis: Long, delayedTimeHours: Int, delayedTimeMinutes: Int): LocalDateTime {
        val instant = Instant.fromEpochMilliseconds(dateMillis)
        val date = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val delayedHidingAt = LocalDateTime(
            year = date.year,
            monthNumber = date.monthNumber,
            dayOfMonth = date.dayOfMonth,
            hour = delayedTimeHours,
            minute = delayedTimeMinutes,
        )

        return delayedHidingAt
    }

    private fun setErrorDialogStateForCreateContentLoading(error: GetUserHierarchyResponses? = null) {
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

    private fun constructCreateContentLoadingErrorDialogContent(error: GetUserHierarchyResponses?): String {
        return when (error) {
            GetUserHierarchyResponses.GetUsergroupHierarchyForbidden -> "У вас недостаточно прав для загрузки аудитории объявления"
            else -> "Непредвиденная ошибка при загрузке контента для создания объявления. Повторите попытку"
        }
    }

    private fun setErrorDialogStateForAnnouncementCreating(error: CreateAnnouncementErrors? = null) {
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

    private fun constructCreateErrorDialogContent(error: CreateAnnouncementErrors?): String {
        return when (error) {
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
    }
}