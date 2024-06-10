package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses

enum class GetAnnouncementEditContentErrors {
    // 403
    /** Пользователь не имеет права получить данные для редактирования объявление */
    GetAnnouncementUpdateContentForbidden,
    // 404
    /** Объявление не существует */
    AnnouncementDoesNotExist;
}