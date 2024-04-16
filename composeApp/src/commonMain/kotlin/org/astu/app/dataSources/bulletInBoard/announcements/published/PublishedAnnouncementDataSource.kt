package org.astu.app.dataSources.bulletInBoard.announcements.published

import com.benasher44.uuid.Uuid
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementDetails
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementSummary

interface PublishedAnnouncementDataSource {
    suspend fun getList(): List<AnnouncementSummary>
    suspend fun getDetails(id: Uuid): AnnouncementDetails
}