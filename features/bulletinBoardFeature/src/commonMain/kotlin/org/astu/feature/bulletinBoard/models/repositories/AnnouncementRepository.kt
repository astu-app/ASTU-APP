package org.astu.feature.bulletinBoard.models.repositories

import androidx.compose.runtime.mutableStateOf
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.CreateAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.GetAnnouncementDetailsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.GetPostedAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.announcements.general.ApiGeneralAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.announcements.published.ApiPublishedAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementDetails
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.feature.bulletinBoard.models.entities.attachments.file.File
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.QuestionDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import org.astu.feature.bulletinBoard.viewModels.humanization.humanizeFileSize
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentBase
import org.astu.feature.bulletinBoard.views.components.attachments.files.models.FileDownloadState
import org.astu.feature.bulletinBoard.views.components.attachments.files.models.FileSummary
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.answers.models.MultipleChoiceAnswerContent
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.answers.models.SingleChoiceAnswerContent
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.answers.models.VotedAnswerContentSummary
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.common.models.SurveyContent
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.questions.models.MultipleChoiceQuestionContent
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.questions.models.QuestionContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.questions.models.SingleChoiceQuestionContent
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.questions.models.VotedQuestionContent
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent
import kotlin.math.roundToInt

class AnnouncementRepository {
    private val publishedAnnouncementsSource = ApiPublishedAnnouncementDataSource()
    private val generalAnnouncementsSource = ApiGeneralAnnouncementDataSource()

    suspend fun loadList(): ContentWithError<List<AnnouncementSummaryContent>, GetPostedAnnouncementListErrors> {
        val content = publishedAnnouncementsSource.getList()
        val announcements = content.content?.map {
            AnnouncementSummaryContent(it.id, it.author, it.publicationTime, it.text, it.viewed, it.audienceSize, mapAttachments(it.files, it.surveys))
        }

        return ContentWithError(announcements, content.error)
    }

    suspend fun loadDetails(id: Uuid): ContentWithError<AnnouncementDetails, GetAnnouncementDetailsErrors> {
        return publishedAnnouncementsSource.getDetails(id)
    }

    suspend fun create(announcement: CreateAnnouncement): CreateAnnouncementErrors? {
        return generalAnnouncementsSource.create(announcement)
    }

    fun update() {

    }



    private fun mapAttachments(files: List<File>?, surveys: List<SurveyDetails>?): List<AttachmentBase> {
        val attachments = mutableListOf<AttachmentBase>()
        attachments.addAll(mapFiles(files))
        attachments.addAll(mapSurveys(surveys))

        return attachments
    }

    private fun mapFiles(files: List<File>?): List<AttachmentBase> {
        if (files == null) return emptyList()
        return files.map {
            FileSummary(it.id, it.name, humanizeFileSize(it.sizeInBytes), mutableStateOf(FileDownloadState.DOWNLOADED))
        }
    }

    private fun mapSurveys(surveys: List<SurveyDetails>?): List<SurveyContent> {
        if (surveys == null) return emptyList()
        return surveys.map { SurveyContent(mapQuestions(it.questions, it.votersAmount, it.voteFinishedAt != null)) }
    }

    private fun mapQuestions(questions: List<QuestionDetails>, votersAmount: Int, voteFinished: Boolean): List<QuestionContentBase> {
        return questions.map { question ->
            if (voteFinished) {
                mapVotedQuestion(question, votersAmount)
            } else if (question.isMultipleChoiceAllowed) {
                mapMultipleChoiceQuestion(question)
            } else {
                mapSingleChoiceQuestion(question)
            }
        }
    }

    private fun mapVotedQuestion(question: QuestionDetails, votersAmount: Int): VotedQuestionContent {
        val answers = question.answers.map { answer -> VotedAnswerContentSummary(answer.content, (answer.votersAmount.toDouble() /  votersAmount).roundToInt()) }
        return VotedQuestionContent(question.content, answers)
    }

    private fun mapMultipleChoiceQuestion(question: QuestionDetails): MultipleChoiceQuestionContent {
        val answers = question.answers.map { answer ->
            MultipleChoiceAnswerContent(
                answer.content,
                mutableStateOf(false)
            )
        }
        return MultipleChoiceQuestionContent(question.content, answers)
    }

    private fun mapSingleChoiceQuestion(question: QuestionDetails): SingleChoiceQuestionContent {
        val answers = question.answers.map { answer -> SingleChoiceAnswerContent(answer.content) }
        return SingleChoiceQuestionContent(question.content, answers)
    }
}