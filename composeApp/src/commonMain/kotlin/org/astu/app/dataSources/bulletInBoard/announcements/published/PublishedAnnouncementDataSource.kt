package org.astu.app.dataSources.bulletInBoard.announcements.published

import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementSummary

interface PublishedAnnouncementDataSource {
    suspend fun getList(): List<AnnouncementSummary>
}