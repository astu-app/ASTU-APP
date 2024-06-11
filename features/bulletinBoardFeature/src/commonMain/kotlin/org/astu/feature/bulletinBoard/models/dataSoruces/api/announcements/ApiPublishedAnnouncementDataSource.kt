package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements

import co.touchlab.kermit.Logger
import com.benasher44.uuid.Uuid
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.feature.bulletinBoard.models.dataSoruces.PublishedAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.AnnouncementMappers.toModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.AnnouncementMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.AnnouncementDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.AnnouncementSummaryDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetAnnouncementDetailsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetPostedAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.HidePostedAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.readUnsuccessCode
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementDetails
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementSummary
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.SecurityHttpClient

class ApiPublishedAnnouncementDataSource(private val baseUrl: String) : PublishedAnnouncementDataSource {
    private val securityHttpClient by GlobalDIContext.inject<SecurityHttpClient>()
    private val client = securityHttpClient.instance


    override suspend fun getList(): ContentWithError<List<AnnouncementSummary>, GetPostedAnnouncementListErrors> {
        val response = client.get("${baseUrl}/api/bulletin-board-service/announcements/published/get-list")

        if (!response.status.isSuccess()) {
            return ContentWithError(null, error = readUnsuccessCode<GetPostedAnnouncementListErrors>(response))
        }

        try {
            val announcements = response.body<Array<AnnouncementSummaryDto>>().toModels()
            return ContentWithError(announcements, null)
        } catch (e: Exception) {
            Logger.e(e.message ?: "empty message", e)
            throw e
        }
    }

    override suspend fun getDetails(id: Uuid): ContentWithError<AnnouncementDetails, GetAnnouncementDetailsErrors> {
        val response = client.get("${baseUrl}/api/bulletin-board-service/announcements/get-details/$id") {
            contentType(ContentType.Application.Json)
        }

        if (!response.status.isSuccess()) {
            return ContentWithError(null, error = readUnsuccessCode<GetAnnouncementDetailsErrors>(response))
        }

        try {
            val dto = response.body<AnnouncementDetailsDto>()
            return ContentWithError(dto.toModel(), error = null)
        } catch (e: Exception) {
            Logger.e(e.message ?: "empty message", e)
            throw e
        }
    }

    override suspend fun hide(id: Uuid): HidePostedAnnouncementErrors? {
        val response = client.post("${baseUrl}/api/bulletin-board-service/announcements/published/hide") {
            contentType(ContentType.Application.Json)
            setBody("\"$id\"")
        }

        if (!response.status.isSuccess()) {
            return readUnsuccessCode<HidePostedAnnouncementErrors>(response)
        }

        return null
    }
}