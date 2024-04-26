package org.astu.app.dataSources.bulletInBoard.announcements.published

import com.benasher44.uuid.Uuid
import org.astu.app.dataSources.bulletInBoard.announcements.common.responses.GetAnnouncementDetailsErrors
import org.astu.app.dataSources.bulletInBoard.announcements.common.responses.GetPostedAnnouncementListErrors
import org.astu.app.dataSources.bulletInBoard.common.responses.ContentWithError
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementDetails
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementSummary

interface PublishedAnnouncementDataSource {
    /**
     * Выполнить запрос на получение списка опубликованных объявлений
     * @return Полученный список объявления или null, если запрос выполнен неуспешно
     */
    suspend fun getList(): ContentWithError<List<AnnouncementSummary>, GetPostedAnnouncementListErrors>

    /**
     * Выполнить запрос на получение деталей объявления
     * @param id Идентификатор объявления
     * @return Полученные детали объявления или null, если запрос выполнен неуспешно
     */
    suspend fun getDetails(id: Uuid): ContentWithError<AnnouncementDetails, GetAnnouncementDetailsErrors>
}