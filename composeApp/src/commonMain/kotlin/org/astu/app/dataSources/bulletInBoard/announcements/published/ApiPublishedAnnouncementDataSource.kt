package org.astu.app.dataSources.bulletInBoard.announcements.published

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.app.GlobalDIContext
import org.astu.app.dataSources.bulletInBoard.announcements.common.dtos.AnnouncementDetailsDto
import org.astu.app.dataSources.bulletInBoard.announcements.common.dtos.AnnouncementSummaryDto
import org.astu.app.dataSources.bulletInBoard.announcements.common.dtos.UserSummaryDto
import org.astu.app.dataSources.bulletInBoard.attachments.files.dtos.FileSummaryDto
import org.astu.app.dataSources.bulletInBoard.attachments.surveys.dtos.QuestionAnswerDetailsDto
import org.astu.app.dataSources.bulletInBoard.attachments.surveys.dtos.QuestionDetailsDto
import org.astu.app.dataSources.bulletInBoard.attachments.surveys.dtos.SurveyDetailsDto
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementDetails
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementSummary
import org.astu.app.models.bulletInBoard.entities.attachments.file.File
import org.astu.app.models.bulletInBoard.entities.attachments.file.FileType
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.AnswerDetails
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.QuestionDetails
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.SurveyDetails
import org.astu.app.models.bulletInBoard.entities.audience.User
import org.kodein.di.instance

class ApiPublishedAnnouncementDataSource : PublishedAnnouncementDataSource {
    private val client: HttpClient by GlobalDIContext.instance()


    override suspend fun getList(): List<AnnouncementSummary> {
        val response = client.get("api/announcements/published/get-list")
        return response.body<Array<AnnouncementSummaryDto>>().map { a ->
            AnnouncementSummary(uuidFrom(a.id), a.authorName, a.publishedAt, a.content, a.viewsCount, a.audienceSize, mapFiles(a.files), mapSurveys(a.surveys))
        }
    }

    override suspend fun getDetails(id: Uuid): AnnouncementDetails {
        val response = client.get("api/announcements/get-details/$id") {
            contentType(ContentType.Application.Json)
        }

        val dto = response.body<AnnouncementDetailsDto>()
        return AnnouncementDetails(
            id = uuidFrom(dto.id),
            content = dto.content,
            authorName = dto.authorName,
            viewsCount = dto.viewsCount,
            audienceSize = dto.audienceSize,
            files = mapFiles(dto.files),
            surveys = mapSurveys(dto.surveys),
            publishedAt = dto.publishedAt,
            hiddenAt = dto.hiddenAt,
            delayedPublishingAt = dto.delayedPublishingAt,
            delayedHidingAt = dto.delayedHidingAt,
            audience = mapAudience(dto.audience)
        )
    }



    private fun mapFiles(files: List<FileSummaryDto>?): List<File> {
        if (files.isNullOrEmpty()) return emptyList()
        return  files.map { f -> File(uuidFrom(f.id), f.name, FileType.fromInt(f.type), 20_971_520) }
    }

    private fun mapSurveys(surveys: List<SurveyDetailsDto>?): List<SurveyDetails> {
        if (surveys == null) return emptyList()
        return  surveys.map { s -> SurveyDetails(uuidFrom(s.id), s.isOpen, s.isAnonymous, s.votersAmount, s.autoClosingAt, s.voteFinishedAt, mapQuestions(s.questions)) }
    }

    private fun mapQuestions(questions: List<QuestionDetailsDto>): List<QuestionDetails> {
        return questions.map { QuestionDetails(uuidFrom(it.id), it.content, it.isMultipleChoiceAllowed, mapAnswers(it.answers)) }
    }

    private fun mapAnswers(answers: List<QuestionAnswerDetailsDto>): List<AnswerDetails> {
        return answers.map { AnswerDetails(uuidFrom(it.id), it.content, it.votersAmount) }
    }

    private fun mapAudience(audience: List<UserSummaryDto>): List<User> {
        return audience.map { u -> User(uuidFrom(u.id), u.firstName, u.secondName, u.patronymic) }
    }
}