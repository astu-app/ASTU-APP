package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import io.ktor.client.*
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
import org.astu.infrastructure.GlobalDIContext

class ApiSurveyDataSource : SurveyDataSource {
    private val client by GlobalDIContext.inject<HttpClient>()

    override suspend fun create(survey: CreateSurvey): ContentWithError<Uuid, CreateSurveyErrors> {
        var dto = survey.toDto()
        val response = client.post("api/surveys/create") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess())
            return ContentWithError(content = null, readUnsuccessCode<CreateSurveyErrors>(response));

        val id = response.body<String>()
        val idTrimmed = id.trim('"');
        return ContentWithError(uuidFrom(idTrimmed), error = null)
    }

    override suspend fun vote(votes: VoteInSurvey): VoteInSurveyErrors? {
        val dto = votes.toDto()
        val response = client.post("api/surveys/vote") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess())
            return readUnsuccessCode<VoteInSurveyErrors>(response)

        return null;
    }

    override suspend fun close(id: Uuid): CloseSurveyErrors? {
        val dto = id.toString()
        val response = client.post("api/surveys/close-survey") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess())
            return readUnsuccessCode<CloseSurveyErrors>(response)

        return null;
    }

    override suspend fun downloadResults(id: Uuid): DownloadSurveyResultsErrors? {
        TODO("Download survey results not yet implemented")
    }
}