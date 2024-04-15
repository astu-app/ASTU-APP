package org.astu.app.dataSources.bulletInBoard.announcements.published

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import com.benasher44.uuid.uuidFrom
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.util.*
import org.astu.app.GlobalDIContext
import org.astu.app.dataSources.bulletInBoard.announcements.common.dtos.AnnouncementSummaryDto
import org.astu.app.dataSources.bulletInBoard.attachments.files.dtos.FileSummaryDto
import org.astu.app.dataSources.bulletInBoard.attachments.surveys.dtos.QuestionAnswerDetailsDto
import org.astu.app.dataSources.bulletInBoard.attachments.surveys.dtos.QuestionDetailsDto
import org.astu.app.dataSources.bulletInBoard.attachments.surveys.dtos.SurveyDetailsDto
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementSummary
import org.astu.app.models.bulletInBoard.entities.attachments.file.File
import org.astu.app.models.bulletInBoard.entities.attachments.file.FileType
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.AnswerDetails
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.QuestionDetails
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.SurveyDetails
import org.kodein.di.instance

class ApiPublishedAnnouncementDataSource : PublishedAnnouncementDataSource {
    private val client: HttpClient by GlobalDIContext.instance()


    @OptIn(InternalAPI::class)
    override suspend fun getList(): List<AnnouncementSummary> {
//        val response = client.get("https://192.168.1.12:7222/api/announcements/published/get-list")
        val response = client.get("https://192.168.1.11:7222/api/announcements/published/get-list")
        Logger.log(Severity.Debug, "json", null, response.content.readUTF8Line(Int.MAX_VALUE)!!) // remove

        return response.body<Array<AnnouncementSummaryDto>>().map { a ->
            AnnouncementSummary(uuidFrom(a.id), a.authorName, a.publishedAt, a.content, a.viewsCount, a.audienceSize, mapFiles(a.files), mapSurveys(a.surveys))
        }
    }



    private fun mapFiles(files: List<FileSummaryDto>?): List<File>? {
        if (files.isNullOrEmpty()) return null
        return  files.map { f -> File(uuidFrom(f.id), f.name, FileType.fromInt(f.type), 20_971_520) }
    }

    private fun mapSurveys(surveys: List<SurveyDetailsDto>?): List<SurveyDetails>? {
        if (surveys == null) return null
        return  surveys.map { s -> SurveyDetails(uuidFrom(s.id), s.isOpen, s.isAnonymous, s.votersAmount, s.autoClosingAt, s.voteFinishedAt, mapQuestions(s.questions)) }
    }

    private fun mapQuestions(questions: List<QuestionDetailsDto>): List<QuestionDetails> {
        return questions.map { QuestionDetails(uuidFrom(it.id), it.content, it.isMultipleChoiceAllowed, mapAnswers(it.answers)) }
    }

    private fun mapAnswers(answers: List<QuestionAnswerDetailsDto>): List<AnswerDetails> {
        return answers.map { AnswerDetails(uuidFrom(it.id), it.content, it.votersAmount) }
    }
}