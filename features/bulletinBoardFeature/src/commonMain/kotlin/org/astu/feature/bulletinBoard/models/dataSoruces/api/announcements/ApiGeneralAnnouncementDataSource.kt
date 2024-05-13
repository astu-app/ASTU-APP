package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements

import com.benasher44.uuid.Uuid
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.feature.bulletinBoard.models.dataSoruces.GeneralAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.AnnouncementMappers.toDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.AnnouncementMappers.toModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.ContentForAnnouncementUpdatingDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.*
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.files.responses.UploadFilesErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.ApiSurveyDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CreateSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.readUnsuccessCode
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.feature.bulletinBoard.models.entities.announcements.EditAnnouncement
import org.astu.feature.bulletinBoard.models.entities.attachments.file.creation.CreateFile
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateSurvey
import org.astu.infrastructure.GlobalDIContext

class ApiGeneralAnnouncementDataSource : GeneralAnnouncementDataSource {
    private val client: HttpClient by GlobalDIContext.inject<HttpClient>()

    private val surveyDataSource = ApiSurveyDataSource()


    override suspend fun create(announcement: CreateAnnouncement): CreateAnnouncementErrorsAggregate? {
        val newSurveyIdContent = if (announcement.survey != null) uploadSurvey(announcement.survey) else null
        if (newSurveyIdContent != null && !newSurveyIdContent.isContentValid) {
            return CreateAnnouncementErrorsAggregate(createSurveyError = newSurveyIdContent.error)
        }
        val newFileIdsContent = if (announcement.files != null) uploadFiles(announcement.files) else null
        if (newFileIdsContent != null && !newFileIdsContent.isContentValid) {
            return CreateAnnouncementErrorsAggregate(createFilesError = newFileIdsContent.error)
        }

        val newSurveyId = newSurveyIdContent?.content
        val newFileIds = newFileIdsContent?.content

        val dto = announcement.toDto(constructAttachmentIds(newSurveyId?.toString(), newFileIds))
        val response = client.post("api/announcements/create") {
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
        val response = client.get("api/announcements/get-update-content/$id") {
            accept(ContentType.Application.Json)
        }

        if (!response.status.isSuccess())
            return ContentWithError(null, readUnsuccessCode<EditAnnouncementErrors>(response))

        val dto = response.body<ContentForAnnouncementUpdatingDto>();
        return ContentWithError(dto.toModel(), error = null,)
    }

    override suspend fun edit(announcement: EditAnnouncement): EditAnnouncementErrorsAggregate? {
        val newSurveyIdContent = if (announcement.newSurvey != null) uploadSurvey(announcement.newSurvey) else null
        if (newSurveyIdContent != null && !newSurveyIdContent.isContentValid) {
            return EditAnnouncementErrorsAggregate(createSurveyError = newSurveyIdContent.error)
        }
        val newFileIdsContent = if (announcement.newFiles != null) uploadFiles(announcement.newFiles) else null
        if (newFileIdsContent != null && !newFileIdsContent.isContentValid) {
            return EditAnnouncementErrorsAggregate(createFilesError = newFileIdsContent.error)
        }

        val newSurveyId = newSurveyIdContent?.content
        val newFileIds = newFileIdsContent?.content

        val dto = announcement.toDto(constructAttachmentIds(newSurveyId?.toString(), newFileIds))
        val response = client.put("api/announcements/update") {
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
        val response = client.delete("api/announcements/delete/") {
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

    private fun uploadFiles(files: List<CreateFile>): ContentWithError<List<String>, UploadFilesErrors> {
        return ContentWithError(content = null, error = null) // todo прикрутить файлы
    }

    private fun constructAttachmentIds(surveyId: String?, fileIds: List<String>?): List<String> {
        /*
         * всевозможные варианты:
         * null, null
         * null, not null
         * not null, null
         * not null, not null
         */

        if (surveyId == null && fileIds == null) return emptyList()
        if (surveyId == null && fileIds != null) return fileIds
        if (surveyId != null && fileIds == null) return listOf(surveyId)

        return fileIds!!.plusElement(surveyId!!); // !! так как проверки на null присутствуют выше
    }
}