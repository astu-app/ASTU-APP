package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses

enum class HidePostedAnnouncementErrors {
    // 403
    /** Пользователь не имеет права скрыть объявление */
    AnnouncementHidingForbidden,
    // 404
    /** Объявление не существует */
    AnnouncementDoesNotExist,
    // 409
    /** Объявление уже скрыто */
    AnnouncementAlreadyHidden,
    /** Объявление еще не опубликовано */
    AnnouncementNotYetPublished;
}