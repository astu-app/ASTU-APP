package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses

enum class DeleteAnnouncementErrors {
    // 403
    /** Пользователь не имеет права удалить объявление */
    AnnouncementDeletionForbidden,
    // 409
    /** Объявление не существует */
    AnnouncementDoesNotExist;
}