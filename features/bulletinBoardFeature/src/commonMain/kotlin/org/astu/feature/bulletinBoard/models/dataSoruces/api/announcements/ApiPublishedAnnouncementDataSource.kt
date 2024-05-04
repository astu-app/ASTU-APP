package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.feature.bulletinBoard.models.dataSoruces.PublishedAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.AnnouncementDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.AnnouncementSummaryDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetAnnouncementDetailsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetPostedAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.files.FileMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.AttachmentMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.readUnsuccessCode
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.UserMappers.toModels
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementDetails
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementSummary
import org.astu.infrastructure.GlobalDIContext

class ApiPublishedAnnouncementDataSource : PublishedAnnouncementDataSource {
    private val client: HttpClient by GlobalDIContext.inject<HttpClient>()


    override suspend fun getList(): ContentWithError<List<AnnouncementSummary>, GetPostedAnnouncementListErrors> {
        val response = client.get("api/announcements/published/get-list")

        if (!response.status.isSuccess()) {
            return ContentWithError(null, error = readUnsuccessCode<GetPostedAnnouncementListErrors>(response))
        }

        val announcements = response.body<Array<AnnouncementSummaryDto>>().map { a ->
            AnnouncementSummary(
                uuidFrom(a.id),
                a.authorName,
                a.publishedAt,
                a.content,
                a.viewsCount,
                a.audienceSize,
                a.files.toModels(),
                a.surveys?.toModels() ?: emptyList()
            )
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

        val dto = response.body<AnnouncementDetailsDto>()
        return ContentWithError(
            AnnouncementDetails(
                id = uuidFrom(dto.id),
                content = dto.content,
                authorName = dto.authorName,
                viewsCount = dto.viewsCount,
                audienceSize = dto.audienceSize,
                files = dto.files.toModels(),
                surveys = dto.surveys?.toModels() ?: emptyList(),
                publishedAt = dto.publishedAt,
                hiddenAt = dto.hiddenAt,
                delayedPublishingAt = dto.delayedPublishingAt,
                delayedHidingAt = dto.delayedHidingAt,
                audience = dto.audience.toModels()
            ),
            error = null
        )
    }
}