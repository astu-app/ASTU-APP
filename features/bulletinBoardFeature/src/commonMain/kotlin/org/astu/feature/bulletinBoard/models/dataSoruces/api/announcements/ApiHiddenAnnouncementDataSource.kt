package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements

import com.benasher44.uuid.Uuid
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.feature.bulletinBoard.models.dataSoruces.HiddenAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.AnnouncementMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.AnnouncementSummaryDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetHiddenAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.RestoreHiddenAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.readUnsuccessCode
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementSummary
import org.astu.infrastructure.GlobalDIContext

class ApiHiddenAnnouncementDataSource : HiddenAnnouncementDataSource {
    private val client: HttpClient by GlobalDIContext.inject<HttpClient>()


    override suspend fun getList(): ContentWithError<List<AnnouncementSummary>, GetHiddenAnnouncementListErrors> {
        val response = client.get("api/announcements/hidden/get-list")

        if (!response.status.isSuccess()) {
            return ContentWithError(null, error = readUnsuccessCode<GetHiddenAnnouncementListErrors>(response))
        }

        val announcements = response.body<Array<AnnouncementSummaryDto>>().toModels()
        return ContentWithError(announcements, null)
    }

    override suspend fun restore(id: Uuid): RestoreHiddenAnnouncementErrors? {
        val dto = "\"$id\""
        val response = client.post("api/announcements/hidden/restore") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess()) {
            return readUnsuccessCode<RestoreHiddenAnnouncementErrors>(response)
        }

        return null
    }
}