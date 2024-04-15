package org.astu.app.models.bulletInBoard.entities.announcements

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime

/**
 * @param content Текстовое содержимое объявления
 * @param userIds Идентификаторы пользователей, для которых создается объявление
 * @param attachmentIds Идентификаторы категорий объявлений
 * @param delayedPublishingAt Срок отложенной публикации объявления
 * @param delayedHidingAt Срок отложенного сокрытия объявления
 */
data class CreateAnnouncement (
    val content: String,
    val userIds: List<Uuid>,
    val attachmentIds: List<Uuid>,
    val delayedPublishingAt: LocalDateTime,
    val delayedHidingAt: LocalDateTime,
    // todo добавить категории объявлений
)