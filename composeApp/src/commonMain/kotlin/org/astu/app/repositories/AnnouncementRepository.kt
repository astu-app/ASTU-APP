package org.astu.app.repositories

import androidx.compose.runtime.mutableStateOf
import com.benasher44.uuid.Uuid
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.components.bulletinBoard.attachments.files.models.FileDownloadState
import org.astu.app.components.bulletinBoard.attachments.files.models.FileSummary
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.MultipleChoiceAnswerContent
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.SingleChoiceAnswerContent
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.VotedAnswerContentSummary
import org.astu.app.components.bulletinBoard.attachments.surveys.common.models.SurveyContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.MultipleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.QuestionContentBase
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.SingleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.VotedQuestionContent
import org.astu.app.dataSources.bulletInBoard.announcements.published.ApiPublishedAnnouncementDataSource
import org.astu.app.entities.bulletInBoard.announcement.summary.AnnouncementSummaryContent
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementDetails
import org.astu.app.models.bulletInBoard.entities.announcements.CreateAnnouncement
import org.astu.app.models.bulletInBoard.entities.attachments.file.File
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.QuestionDetails
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.SurveyDetails
import org.astu.app.view_models.bulletInBoard.humanization.humanizeFileSize
import kotlin.math.roundToInt

class AnnouncementRepository {
    private val publishedAnnouncementsSource = ApiPublishedAnnouncementDataSource()

    suspend fun loadList(): List<AnnouncementSummaryContent> {
        return publishedAnnouncementsSource.getList().map {
            AnnouncementSummaryContent(it.id, it.author, it.publicationTime, it.text, it.viewed, it.audienceSize, mapAttachments(it.files, it.surveys))
        }

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

    private fun mapSurveys(surveys: List<SurveyDetails>?): List<AttachmentBase> {
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



    suspend fun loadDetails(id: Uuid): AnnouncementDetails {
        return publishedAnnouncementsSource.getDetails(id)
    }

    fun create(announcement: CreateAnnouncement) {

    }

    fun update() {

    }
}