package org.astu.feature.bulletinBoard.models.dataSoruces

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetDelayedHiddenAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetDelayedPublishedAnnouncementsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.PublishImmediatelyDelayedAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementSummary

interface DelayedAnnouncementsDataSource {
    /**
     * Выполнить запрос на получение списка объявлений, ожидающих отложенную публикацию
     * @return Полученный список объявлений или null, если запрос выполнен неуспешно
     */
    suspend fun getDelayedPublishingList(): ContentWithError<List<AnnouncementSummary>, GetDelayedPublishedAnnouncementsErrors>

    /**
     * Выполнить запрос на немедленную публикацию объявления, ожидающего отложенное сокрытие
     * @return Код ошибки или null, если запрос выполнен неуспешно
     */
    suspend fun publishImmediately(id: Uuid): PublishImmediatelyDelayedAnnouncementErrors?

    /**
     * Выполнить запрос на получение списка объявлений, ожидающих отложенное сокрытие
     * @return Полученный список объявлений или null, если запрос выполнен неуспешно
     */
    suspend fun getDelayedHiddenList(): ContentWithError<List<AnnouncementSummary>, GetDelayedHiddenAnnouncementListErrors>
}