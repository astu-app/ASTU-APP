package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements

import com.benasher44.uuid.Uuid
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.feature.bulletinBoard.models.dataSoruces.DelayedAnnouncementsDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.AnnouncementMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.AnnouncementSummaryDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetDelayedHiddenAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetDelayedPublishedAnnouncementsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.PublishImmediatelyDelayedAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.readUnsuccessCode
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementSummary
import org.astu.infrastructure.DependencyInjection.GlobalDIContext

class ApiDelayedAnnouncementsDataSource : DelayedAnnouncementsDataSource {
    private val client: HttpClient by GlobalDIContext.inject<HttpClient>()

    override suspend fun getDelayedPublishingList(): ContentWithError<List<AnnouncementSummary>, GetDelayedPublishedAnnouncementsErrors> {
        val response = client.get("api/announcements/delayed-publishing/get-list")

        if (!response.status.isSuccess()) {
            return ContentWithError(null, error = readUnsuccessCode<GetDelayedPublishedAnnouncementsErrors>(response))
        }

        val announcements = response.body<Array<AnnouncementSummaryDto>>().toModels()
        return ContentWithError(announcements, null)
    }

    override suspend fun publishImmediately(id: Uuid): PublishImmediatelyDelayedAnnouncementErrors? {
        val dto = "\"$id\""
        val response = client.post("api/announcements/delayed-publishing/publish-immediately") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess()) {
            return readUnsuccessCode<PublishImmediatelyDelayedAnnouncementErrors>(response)
        }

        return null
    }

    override suspend fun getDelayedHiddenList(): ContentWithError<List<AnnouncementSummary>, GetDelayedHiddenAnnouncementListErrors> {
        val response = client.get("api/announcements/delayed-hidden/get-list")

        if (!response.status.isSuccess()) {
            return ContentWithError(null, error = readUnsuccessCode<GetDelayedHiddenAnnouncementListErrors>(response))
        }

        val announcements = response.body<Array<AnnouncementSummaryDto>>().toModels()
        return ContentWithError(announcements, null)
    }
}