package org.astu.app.dataSources.bulletInBoard.announcements.general

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.app.GlobalDIContext
import org.astu.app.dataSources.bulletInBoard.announcements.common.responses.CreateAnnouncementErrors
import org.astu.app.dataSources.bulletInBoard.common.readUnsuccessCode
import org.astu.app.models.bulletInBoard.entities.announcements.CreateAnnouncement
import org.kodein.di.instance

class ApiGeneralAnnouncementDataSource : GeneralAnnouncementDataSource {
    private val client: HttpClient by GlobalDIContext.instance()


    override suspend fun create(announcement: CreateAnnouncement): CreateAnnouncementErrors? {
        val response = client.post("api/announcements/create") {
            contentType(ContentType.Application.Json)
            setBody(announcement)
        }

        if (response.status.isSuccess())
            return null

        return readUnsuccessCode<CreateAnnouncementErrors>(response)
    }
}