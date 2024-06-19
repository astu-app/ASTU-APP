package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.details.SurveyDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.dtos.UserSummaryDto

/**
 * @param id Идентификатор объявления
 * @param author Автор объявления
 * @param content Текстовое содержимое объявления
 * @param publishedAt Время публикации объявления
 * @param hiddenAt Время сокрытия объявления
 * @param delayedPublishingAt Момент отложенной публикации объявления
 * @param delayedHidingAt Момент отложенного сокрытия объявления
 * @param viewsCount Количество просмотревших объявление пользователей
 * @param audienceSize Размер аудитории объявления
 * @param surveys Список опросов, прикрепленных к объявлению
 */
@Serializable
data class AnnouncementSummaryDto (
    val id: String,
    val author: UserSummaryDto,
    val content: String,
    val publishedAt: LocalDateTime?,
    val hiddenAt: LocalDateTime?,
    val delayedPublishingAt: LocalDateTime?,
    val delayedHidingAt: LocalDateTime?,
    val viewsCount: Int,
    val audienceSize: Int,
    val surveys: List<SurveyDetailsDto>?,
)