package org.astu.feature.bulletinBoard.models.dataSoruces

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetAnnouncementDetailsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetPostedAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementDetails
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementSummary

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