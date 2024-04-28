package org.astu.feature.bulletinBoard.models.dataSoruces.announcements.general

import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement


interface GeneralAnnouncementDataSource {
    /**
     * Выполнить запрос на создание объявления
     * @param announcement данные для создания объявления
     * @return Код неуспешного ответа или null, если запрос выполнен успешно
     */
    suspend fun create(announcement: CreateAnnouncement): org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.CreateAnnouncementErrors?
}