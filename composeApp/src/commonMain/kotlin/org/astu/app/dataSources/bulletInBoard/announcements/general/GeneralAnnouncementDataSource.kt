package org.astu.app.dataSources.bulletInBoard.announcements.general

import org.astu.app.dataSources.bulletInBoard.announcements.common.responses.CreateAnnouncementErrors
import org.astu.app.models.bulletInBoard.entities.announcements.CreateAnnouncement

interface GeneralAnnouncementDataSource {
    /**
     * Выполнить запрос на создание объявления
     * @param announcement данные для создания объявления
     * @return Код неуспешного ответа или null, если запрос выполнен успешно
     */
    suspend fun create(announcement: CreateAnnouncement): CreateAnnouncementErrors?
}