package org.astu.feature.bulletinBoard.models.dataSoruces.announcements.general

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.feature.bulletinBoard.models.dataSoruces.common.readUnsuccessCode
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.infrastructure.GlobalDIContext

class ApiGeneralAnnouncementDataSource :
    org.astu.feature.bulletinBoard.models.dataSoruces.announcements.general.GeneralAnnouncementDataSource {
    private val client: HttpClient by GlobalDIContext.inject<HttpClient>()


    override suspend fun create(announcement: CreateAnnouncement): org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.CreateAnnouncementErrors? {
        val response = client.post("api/announcements/create") {
            contentType(ContentType.Application.Json)
            setBody(announcement)
        }

        if (response.status.isSuccess())
            return null

        return readUnsuccessCode<org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.CreateAnnouncementErrors>(response)
    }
}