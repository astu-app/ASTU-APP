package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses

enum class PublishImmediatelyDelayedAnnouncementErrors {
    // 403
    /** Пользователь не имеет права незамедлительно опубликовать отложенное объявление */
    ImmediatePublishingForbidden,
    // 404
    /** Объявление не существует */
    AnnouncementDoesNotExist;
}