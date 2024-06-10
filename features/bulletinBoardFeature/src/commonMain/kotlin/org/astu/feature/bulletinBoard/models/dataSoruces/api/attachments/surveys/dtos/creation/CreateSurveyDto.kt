package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.creation

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * DTO для создания опроса
 * @param questions Вопросы опроса
 * @param isAnonymous Анонимен ли опрос
 * @param resultsOpenBeforeClosing Открыты ли результаты опроса до закряти
 * @param voteUntil Срок окончания голосования. Null, если голосование не ограничено по сроку
 */
@Serializable
data class CreateSurveyDto (
    val questions: List<CreateQuestionDto>,
    val isAnonymous: Boolean,
    val resultsOpenBeforeClosing: Boolean,
    val voteUntil: LocalDateTime?,
)

