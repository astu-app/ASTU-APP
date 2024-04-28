package org.astu.feature.bulletinBoard.models.dataSoruces.attachments.surveys.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * @param id Идентификатор опроса
 * @param isOpen Открыт ли опрос
 * @param isAnonymous Анонимен ли опрос
 * @param votersAmount Количество проголосовавших в опросе пользователей
 * @param autoClosingAt Время окончания голосования (если задано)
 * @param voteFinishedAt Фактическое время окончания голосования (если голосование завершено)
 * @param questions Вопросы опроса
 */
@Serializable
data class SurveyDetailsDto (
    val id: String,
    val isOpen: Boolean,
    val isAnonymous: Boolean,
    val votersAmount: Int,
    val autoClosingAt: LocalDateTime?,
    val voteFinishedAt: LocalDateTime?,
    val questions: List<QuestionDetailsDto>
)