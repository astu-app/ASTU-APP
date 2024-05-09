package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * @param id Идентификатор опроса
 * @param isVotedByRequester Проголосовал ли в опросе пользователь, запросивший детали опроса
 * @param isOpen Открыт ли опрос
 * @param isAnonymous Анонимен ли опрос
 * @param resultsOpenBeforeClosing Доступны ли результаты опроса до его закрытия
 * @param votersAmount Количество проголосовавших в опросе пользователей
 * @param autoClosingAt Время окончания голосования (если задано)
 * @param voteFinishedAt Фактическое время окончания голосования (если голосование завершено)
 * @param questions Вопросы опроса
 */
@Serializable
data class SurveyDetailsDto (
    val id: String,
    val isVotedByRequester: Boolean,
    val isOpen: Boolean,
    val isAnonymous: Boolean,
    val resultsOpenBeforeClosing: Boolean,
    val votersAmount: Int,
    val autoClosingAt: LocalDateTime?,
    val voteFinishedAt: LocalDateTime?,
    val questions: List<QuestionDetailsDto>
)