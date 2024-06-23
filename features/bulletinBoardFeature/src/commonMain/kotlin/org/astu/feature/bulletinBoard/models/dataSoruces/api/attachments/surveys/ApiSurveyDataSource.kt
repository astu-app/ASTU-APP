package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.feature.bulletinBoard.models.dataSoruces.SurveyDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.SurveyToDtoMappers.toDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CloseSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CreateSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.DownloadSurveyResultsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.VoteInSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.readUnsuccessCode
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateSurvey
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.voting.VoteInSurvey
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.SecurityHttpClient

class ApiSurveyDataSource(private val baseUrl: String) : SurveyDataSource {
    private val securityHttpClient by GlobalDIContext.inject<SecurityHttpClient>()
    private val client = securityHttpClient.instance

    override suspend fun create(survey: CreateSurvey, rootUserGroupId: Uuid): ContentWithError<Uuid, CreateSurveyErrors> {
        val dto = survey.toDto()
        val response = client.post("${baseUrl}/api/bulletin-board-service/surveys/create") {
            headers { append("X-Root-UserGroup-Id", rootUserGroupId.toString()) }
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess())
            return ContentWithError(content = null, readUnsuccessCode<CreateSurveyErrors>(response))

        val id = response.body<String>()
        val idTrimmed = id.trim('"')
        return ContentWithError(uuidFrom(idTrimmed), error = null)
    }

    override suspend fun vote(votes: VoteInSurvey): VoteInSurveyErrors? {
        val dto = votes.toDto()
        val response = client.post("${baseUrl}/api/bulletin-board-service/surveys/vote") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess())
            return readUnsuccessCode<VoteInSurveyErrors>(response)

        return null
    }

    override suspend fun close(id: Uuid): CloseSurveyErrors? {
        val dto = "\"$id\""
        val response = client.post("${baseUrl}/api/bulletin-board-service/surveys/close-survey") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess())
            return readUnsuccessCode<CloseSurveyErrors>(response)

        return null
    }

    override suspend fun downloadResults(id: Uuid): DownloadSurveyResultsErrors? {
        TODO("Download survey results not yet implemented")
    }
}