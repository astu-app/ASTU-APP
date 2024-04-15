package org.astu.app.dataSources.bulletInBoard.announcements.common.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.astu.app.dataSources.bulletInBoard.attachments.files.dtos.FileSummaryDto
import org.astu.app.dataSources.bulletInBoard.attachments.surveys.dtos.SurveyDetailsDto

/**
 * @param id Идентификатор объявления
 * @param authorName Автор объявления
 * @param content Текстовое содержимое объявления
 * @param publishedAt Время публикации объявления
 * @param viewsCount Количество просмотревших объявление пользователей
 * @param audienceSize Размер аудитории объявления
 * @param files Список файлов, прикрепленных к объявлению
 * @param surveys Список опросов, прикрепленных к объявлению
 */
@Serializable
data class AnnouncementSummaryDto (
    val id: String,
    val authorName: String,
    val content: String,
    val publishedAt: LocalDateTime,
    val viewsCount: Int,
    val audienceSize: Int,
    val files: List<FileSummaryDto>?,
    val surveys: List<SurveyDetailsDto>?,
)