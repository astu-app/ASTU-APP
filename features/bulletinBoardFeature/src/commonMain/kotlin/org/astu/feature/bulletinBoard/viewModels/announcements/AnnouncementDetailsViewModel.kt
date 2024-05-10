package org.astu.feature.bulletinBoard.viewModels.announcements

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
import org.astu.feature.bulletinBoard.models.entities.attachments.file.details.File
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.AnswerDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.QuestionDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import org.astu.feature.bulletinBoard.models.entities.audience.User
import org.astu.feature.bulletinBoard.viewModels.humanization.humanizeDateTime
import org.astu.feature.bulletinBoard.viewModels.humanization.humanizeFileSize
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.files.models.AttachedFileContent
import org.astu.feature.bulletinBoard.views.components.attachments.files.models.FileDownloadState
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.VotedAnswerContentDetails
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.QuestionContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.VotedQuestionContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.AttachedSurveyContent
import org.astu.feature.bulletinBoard.views.entities.announcement.details.AnnouncementDetailsContent
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary

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
        mutableState.value = State.Loading
        loadDetails()
    }

    fun loadDetails() {
        screenModelScope.launch {
            mutableState.value = State.Loading
            try {
                val details = model.getDetails(announcementId)
                if (!details.isContentValid) {
                    constructErrorDialogContent(details.error)
                    showErrorDialog.value = true
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
                showErrorDialog.value = true
                constructErrorDialogContent()
            }
        }
    }

    private fun toViewModel(details: AnnouncementDetails): AnnouncementDetailsContent {
        return AnnouncementDetailsContent(
            id = details.id,
            author = details.authorName,
            publicationTime = humanizeDateTime(details.publishedAt),
            viewed = details.viewsCount,
            viewedPercent = calculateVotersPercentage(details.viewsCount, details.audienceSize),
            audienceSize = details.audienceSize,
            text = details.content,
            attachments = attachmentsToViewModel(details.files, details.surveys),
            audience = audienceToViewModel(details.audience),
        )
    }

    private fun audienceToViewModel(audience: List<User>): List<UserSummary> {
        return audience.map { UserSummary(it.id, it.firstName, it.secondName, it.patronymic) }
    }

    private fun attachmentsToViewModel(files: List<File>, surveys: List<SurveyDetails>): List<AttachmentContentBase> {
        val attachments = mutableListOf<AttachmentContentBase>()
        files.forEach {
            // todo получение состояния загрузки файла
            attachments.add(AttachedFileContent(it.id, it.name, humanizeFileSize(it.sizeInBytes), mutableStateOf(
                FileDownloadState.DOWNLOADED)))
        }

        val survey = surveys.firstOrNull() ?: return attachments
        attachments.add(surveyToViewModel(survey))

        return attachments
    }

    private fun surveyToViewModel(survey: SurveyDetails): AttachedSurveyContent {
        return AttachedSurveyContent(survey.id, questionsToViewModel(survey.questions, survey.votersAmount), survey.isVotedByUser)
    }

    private fun questionsToViewModel(questions: List<QuestionDetails>, surveyVotersAmoune: Int): List<QuestionContentBase> {
        return questions.map {
            VotedQuestionContent(
                it.id,
                it.content,
                answersToViewModel(it.answers, surveyVotersAmoune)
            )
        }
    }

    private fun answersToViewModel(answers: List<AnswerDetails>, surveyVotersAmoune: Int): List<VotedAnswerContentDetails> {
        return answers.map {
            VotedAnswerContentDetails(it.id, it.content, calculateVotersPercentage(it.votersAmount, surveyVotersAmoune), null)
        }
    }

    private fun constructErrorDialogContent(error: GetAnnouncementDetailsErrors? = null) {
        errorDialogBody.value = when (error) {
            GetAnnouncementDetailsErrors.DetailsAccessForbidden -> "У вас недостаточно прав для просмотра подробностей этого объявления"
            GetAnnouncementDetailsErrors.AnnouncementDoesNotExist -> "Объявление не найдено"
            else -> unexpectedErrorBody
        }
    }
}