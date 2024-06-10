package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses

enum class RestoreHiddenAnnouncementErrors {
    // 403
    /** Пользователь не имеет права восстановить скрытое объявление */
    RestoreForbidden,
    // 404
    /** Объявление не существует */
    AnnouncementDoesNotExist,
    // 409
    /** Объявление не является скрытым */
    AnnouncementNotHidden;
}