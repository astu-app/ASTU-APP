package org.astu.feature.bulletinBoard.models.dataSoruces

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetHiddenAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.RestoreHiddenAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementSummary

interface HiddenAnnouncementDataSource {
    /**
     * Выполнить запрос на получение списка скрытых объявлений
     * @return Полученный список объявления или null, если запрос выполнен неуспешно
     */
    suspend fun getList(): ContentWithError<List<AnnouncementSummary>, GetHiddenAnnouncementListErrors>

    /**
     * Выполнить запрос на восстановление скрытого объявления
     * @return Код ошибки или null, если запрос выполнен неуспешно
     */
    suspend fun restore(id: Uuid): RestoreHiddenAnnouncementErrors?
}