package org.astu.feature.bulletinBoard.viewModels.announcements.actions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.common.utils.calculateVotersPercentage
import org.astu.feature.bulletinBoard.models.AnnouncementModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetAnnouncementDetailsErrors
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import org.astu.feature.bulletinBoard.models.entities.audience.CheckableUser
import org.astu.feature.bulletinBoard.viewModels.humanization.ErrorCodesHumanization.humanize
import org.astu.feature.bulletinBoard.viewModels.humanization.humanizeDateTime
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.AttachedSurveyContent
import org.astu.feature.bulletinBoard.views.entities.announcement.details.AnnouncementDetailsContent
import org.astu.feature.bulletinBoard.views.entities.attachments.AttachmentToPresentationMappers.toPresentation
import org.astu.feature.bulletinBoard.views.entities.users.CheckableUserSummary

class AnnouncementDetailsViewModel (
    private val announcementId: Uuid
) : StateScreenModel<AnnouncementDetailsViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object Error : State()
    }

    private val model: AnnouncementModel = AnnouncementModel()
    lateinit var content: MutableState<AnnouncementDetailsContent>
    private var contentInitialized: Boolean = false

    private val unexpectedErrorTitle: String = "Ошибка"
    private val unexpectedErrorBody: String = "Непредвиденная ошибка при загрузке подробностей объявления. Повторите попытку"
    val errorDialogLabel: MutableState<String> = mutableStateOf(unexpectedErrorTitle)
    val errorDialogBody: MutableState<String> = mutableStateOf(unexpectedErrorBody)
    val showErrorDialog: MutableState<Boolean> = mutableStateOf(false)

    init {
        loadDetails()
    }

    fun loadDetails() {
        screenModelScope.launch {
            try {
                mutableState.value = State.Loading

                val details = model.getDetails(announcementId)
                if (!details.isContentValid) {
                    constructErrorDialogContent(details.error)
                    mutableState.value = State.Error
                    return@launch
                }

                val detailsViewModel = toViewModel(details.content!!) // так как выше проверка на валидность, которая включает проверку content на not null
                if (contentInitialized) {
                    content.value = detailsViewModel
                } else {
                    content = mutableStateOf(detailsViewModel)
                    contentInitialized = true
                }
                mutableState.value = State.LoadingDone

            } catch (e: Exception) {
                mutableState.value = State.Error
                constructErrorDialogContent()
            }
        }
    }

    private fun toViewModel(details: AnnouncementDetails): AnnouncementDetailsContent {
        val publicationTimeString =
            if (details.publishedAt != null) "Опубликовано ${humanizeDateTime(details.publishedAt)}"
            else "Не опубликовано"
        val hidingTimeString =
            if (details.hiddenAt != null) "Скрыто ${humanizeDateTime(details.hiddenAt)}"
            else "Не скрыто"
        val delayedPublicationTimeString =
            if (details.delayedPublishingAt != null) "Будет опубликовано ${humanizeDateTime(details.delayedPublishingAt)}"
            else "Отложенная публикация не задана"
        val delayedHidingTimeString =
            if (details.delayedHidingAt != null) "Будет скрыто ${humanizeDateTime(details.delayedHidingAt)}"
            else "Отложенное сокрытие не задано"

        return AnnouncementDetailsContent(
            id = details.id,
            author = details.authorName,
            publicationTime = publicationTimeString,
            hidingTime = hidingTimeString,
            delayedPublicationTime = delayedPublicationTimeString,
            delayedHidingTime = delayedHidingTimeString,
            viewed = details.viewsCount,
            viewedPercent = calculateVotersPercentage(details.viewsCount, details.audienceSize),
            audienceSize = details.audienceSize,
            text = details.content,
            attachments = attachmentsToViewModel(details.surveys),
            audience = audienceToViewModel(details.audience),
        )
    }

    private fun audienceToViewModel(audience: List<CheckableUser>): List<CheckableUserSummary> {
        return audience.map {
            CheckableUserSummary(
                id = it.id,
                firstName = it.firstName,
                secondName = it.secondName,
                patronymic = it.patronymic,
                isChecked = it.isChecked
            )
        }
    }

    private fun attachmentsToViewModel(surveys: List<SurveyDetails>): List<AttachmentContentBase> {
        val attachments = mutableListOf<AttachmentContentBase>()

        val survey = surveys.firstOrNull() ?: return attachments
        attachments.add(surveyToViewModel(survey))

        return attachments
    }

    private fun surveyToViewModel(survey: SurveyDetails): AttachedSurveyContent {
        return survey.toPresentation(true, showOnlyResults = true) as AttachedSurveyContent
    }

    private fun constructErrorDialogContent(error: GetAnnouncementDetailsErrors? = null) {
        errorDialogBody.value = error.humanize()
    }
}