package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * Объект содержит контент для создания объявления
 * @param content Текстовое содержимое объявления
 * @param userIds Идентификаторы пользователей, для которых создается объявление
 * @param attachmentIds Идентификаторы категорий объявлений
 * @param delayedPublishingAt Срок отложенной публикации объявления
 * @param delayedHidingAt Срок отложенного сокрытия объявления
 */
@Serializable
data class CreateAnnouncementDto (
    val content: String,
    val userIds: List<String>,
    val attachmentIds: List<String>,
    val delayedPublishingAt: LocalDateTime?,
    val delayedHidingAt: LocalDateTime?,
)