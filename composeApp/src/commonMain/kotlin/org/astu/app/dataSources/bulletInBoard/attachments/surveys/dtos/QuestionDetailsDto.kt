package org.astu.app.dataSources.bulletInBoard.attachments.surveys.dtos

import kotlinx.serialization.Serializable

/**
 * @param id Идентификатор опроса
 * @param content Текстовое содержимое вопроса
 * @param isMultipleChoiceAllowed Разрешен ли множественный выбор
 * @param answers Варианты ответов опроса
 */
@Serializable
data class QuestionDetailsDto (
    val id: String,
    val content: String,
    val isMultipleChoiceAllowed: Boolean,
    val answers: List<QuestionAnswerDetailsDto>
)