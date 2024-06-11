package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements

import com.benasher44.uuid.Uuid
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.feature.bulletinBoard.models.dataSoruces.GeneralAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.AnnouncementMappers.toDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.AnnouncementMappers.toModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.ContentForAnnouncementUpdatingDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.*
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.ApiSurveyDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CreateSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.readUnsuccessCode
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.feature.bulletinBoard.models.entities.announcements.EditAnnouncement
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateSurvey
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.SecurityHttpClient

class ApiGeneralAnnouncementDataSource(private val baseUrl: String) : GeneralAnnouncementDataSource {
    private val securityHttpClient by GlobalDIContext.inject<SecurityHttpClient>()
    private val client = securityHttpClient.instance

    private val surveyDataSource = ApiSurveyDataSource(baseUrl)


    override suspend fun create(announcement: CreateAnnouncement): CreateAnnouncementErrorsAggregate? {
        val newSurveyIdContent = if (announcement.survey != null) uploadSurvey(announcement.survey) else null
        if (newSurveyIdContent != null && !newSurveyIdContent.isContentValid) {
            return CreateAnnouncementErrorsAggregate(createSurveyError = newSurveyIdContent.error)
        }

        val newSurveyId = newSurveyIdContent?.content

        val dto = announcement.toDto(constructAttachmentIds(newSurveyId?.toString()))
        val response = client.post("${baseUrl}/api/bulletin-board-service/announcements/create") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (response.status.isSuccess())
            return null

        return CreateAnnouncementErrorsAggregate(
            createAnnouncementError = readUnsuccessCode<CreateAnnouncementErrors>(
                response
            )
        )
    }

    override suspend fun getUpdateForAnnouncementEditing(id: Uuid): ContentWithError<ContentForAnnouncementEditing, EditAnnouncementErrors> {
        val response = client.get("${baseUrl}/api/bulletin-board-service/announcements/get-update-content/$id") {
            accept(ContentType.Application.Json)
        }

        if (!response.status.isSuccess())
            return ContentWithError(null, readUnsuccessCode<EditAnnouncementErrors>(response))

        val dto = response.body<ContentForAnnouncementUpdatingDto>()
        return ContentWithError(dto.toModel(), error = null,)
    }

    override suspend fun edit(announcement: EditAnnouncement): EditAnnouncementErrorsAggregate? {
        val newSurveyIdContent = if (announcement.newSurvey != null) uploadSurvey(announcement.newSurvey) else null
        if (newSurveyIdContent != null && !newSurveyIdContent.isContentValid) {
            return EditAnnouncementErrorsAggregate(createSurveyError = newSurveyIdContent.error)
        }

        val newSurveyId = newSurveyIdContent?.content

        val dto = announcement.toDto(constructAttachmentIds(newSurveyId?.toString()))
        val response = client.put("${baseUrl}/api/bulletin-board-service/announcements/update") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (response.status.isSuccess())
            return null

        return EditAnnouncementErrorsAggregate(
            editAnnouncementError = readUnsuccessCode<EditAnnouncementErrors>(
                response
            )
        )
    }

    override suspend fun delete(id: Uuid): DeleteAnnouncementErrors? {
        val dto = "\"${id}\""
        val response = client.delete("${baseUrl}/api/bulletin-board-service/announcements/delete") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (response.status.isSuccess())
            return null

        return readUnsuccessCode<DeleteAnnouncementErrors>(response)
    }



    private suspend fun uploadSurvey(survey: CreateSurvey): ContentWithError<Uuid, CreateSurveyErrors> {
        return surveyDataSource.create(survey)
    }

    private fun constructAttachmentIds(surveyId: String?): List<String> = if (surveyId != null)
        listOf(surveyId)
    else
        emptyList()
}