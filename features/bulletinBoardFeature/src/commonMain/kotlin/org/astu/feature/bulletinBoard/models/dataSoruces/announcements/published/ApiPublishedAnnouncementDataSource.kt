package org.astu.feature.bulletinBoard.models.dataSoruces.announcements.published

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.GetAnnouncementDetailsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.GetPostedAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.attachments.files.dtos.FileSummaryDto
import org.astu.feature.bulletinBoard.models.dataSoruces.attachments.surveys.dtos.QuestionAnswerDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.attachments.surveys.dtos.QuestionDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.attachments.surveys.dtos.SurveyDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.common.readUnsuccessCode
import org.astu.feature.bulletinBoard.models.dataSoruces.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.dtos.UserSummaryDto
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementDetails
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementSummary
import org.astu.feature.bulletinBoard.models.entities.attachments.file.File
import org.astu.feature.bulletinBoard.models.entities.attachments.file.FileType
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.AnswerDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.QuestionDetails
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import org.astu.feature.bulletinBoard.models.entities.audience.User
import org.astu.infrastructure.GlobalDIContext

class ApiPublishedAnnouncementDataSource :
    org.astu.feature.bulletinBoard.models.dataSoruces.announcements.published.PublishedAnnouncementDataSource {
    private val client: HttpClient by GlobalDIContext.inject<HttpClient>()


    override suspend fun getList(): ContentWithError<List<AnnouncementSummary>, GetPostedAnnouncementListErrors> {
        val response = client.get("api/announcements/published/get-list")

        if (!response.status.isSuccess()) {
            return ContentWithError(null, error = readUnsuccessCode<GetPostedAnnouncementListErrors>(response))
        }

        val announcements = response.body<Array<org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.dtos.AnnouncementSummaryDto>>().map { a ->
            AnnouncementSummary(uuidFrom(a.id), a.authorName, a.publishedAt, a.content, a.viewsCount, a.audienceSize, mapFiles(a.files), mapSurveys(a.surveys))
        }
        return ContentWithError(announcements, null)
    }

    override suspend fun getDetails(id: Uuid): ContentWithError<AnnouncementDetails, GetAnnouncementDetailsErrors> {
        val response = client.get("api/announcements/get-details/$id") {
            contentType(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) {
            return ContentWithError(null, error = readUnsuccessCode<GetAnnouncementDetailsErrors>(response))
        }

        val dto = response.body<org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.dtos.AnnouncementDetailsDto>()
        return ContentWithError(
            AnnouncementDetails(
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
            ),
            error = null
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
        return questions.map { QuestionDetails(uuidFrom(it.id), it.serial, it.content, it.isMultipleChoiceAllowed, mapAnswers(it.answers)) }
    }

    private fun mapAnswers(answers: List<QuestionAnswerDetailsDto>): List<AnswerDetails> {
        return answers.map { AnswerDetails(uuidFrom(it.id), it.serial, it.content, it.votersAmount) }
    }

    private fun mapAudience(audience: List<UserSummaryDto>): List<User> {
        return audience.map { u -> User(uuidFrom(u.id), u.firstName, u.secondName, u.patronymic) }
    }
}