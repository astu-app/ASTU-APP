package org.astu.feature.bulletinBoard.models.dataSoruces

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.CreateAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.EditAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.feature.bulletinBoard.models.entities.announcements.EditAnnouncement


interface GeneralAnnouncementDataSource {
    /**
     * Выполнить запрос на создание объявления
     * @param announcement данные для создания объявления
     * @return Код неуспешного ответа или null, если запрос выполнен успешно
     */
    suspend fun create(announcement: CreateAnnouncement): CreateAnnouncementErrors?

    /**
     * Выполнить запрос на получение данных для редактирования объявления
     * @param id идентификатор редактируемого объявления
     * @return Полученные данные для редактирования объявления или null, если запрос выполнен неуспешно
     */
    suspend fun getUpdateForAnnouncementEditing(id: Uuid): ContentWithError<ContentForAnnouncementEditing, EditAnnouncementErrors>

    /**
     * Выполнить запрос на редактирование объявления
     * @param announcement данные для редактирования объявления
     * @return Код неуспешного ответа или null, если запрос выполнен успешно
     */
    suspend fun edit(announcement: EditAnnouncement): EditAnnouncementErrors?
}