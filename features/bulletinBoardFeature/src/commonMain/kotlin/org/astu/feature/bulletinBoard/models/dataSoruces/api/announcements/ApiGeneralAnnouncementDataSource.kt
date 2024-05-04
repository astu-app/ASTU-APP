package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements

import com.benasher44.uuid.Uuid
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.feature.bulletinBoard.models.dataSoruces.GeneralAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.AnnouncementMappers.toModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.ContentForAnnouncementUpdatingDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.CreateAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.EditAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.readUnsuccessCode
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.feature.bulletinBoard.models.entities.announcements.EditAnnouncement
import org.astu.infrastructure.GlobalDIContext

class ApiGeneralAnnouncementDataSource : GeneralAnnouncementDataSource {
    private val client: HttpClient by GlobalDIContext.inject<HttpClient>()


    override suspend fun create(announcement: CreateAnnouncement): CreateAnnouncementErrors? {
        val response = client.post("api/announcements/create") {
            contentType(ContentType.Application.Json)
            setBody(announcement)
        }

        if (response.status.isSuccess())
            return null

        return readUnsuccessCode<CreateAnnouncementErrors>(response)
    }

    override suspend fun getUpdateForAnnouncementEditing(id: Uuid): ContentWithError<ContentForAnnouncementEditing, EditAnnouncementErrors> {
        val response = client.get("api/announcements/get-update-content/$id") {
            accept(ContentType.Application.Json)
        }

        if (!response.status.isSuccess())
            return ContentWithError(null, readUnsuccessCode<EditAnnouncementErrors>(response))

        var dto = response.body<ContentForAnnouncementUpdatingDto>();
        return ContentWithError(dto.toModel(), error = null,)
    }

    override suspend fun edit(announcement: EditAnnouncement): EditAnnouncementErrors? {
        val response = client.put("api/announcements/update") {
            contentType(ContentType.Application.Json)
            setBody(announcement)
        }

        if (response.status.isSuccess())
            return null

        return readUnsuccessCode<EditAnnouncementErrors>(response)
    }
}