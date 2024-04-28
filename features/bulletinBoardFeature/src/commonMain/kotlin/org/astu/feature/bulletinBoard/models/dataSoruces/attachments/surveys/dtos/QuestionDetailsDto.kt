package org.astu.feature.bulletinBoard.models.dataSoruces.attachments.surveys.dtos

import kotlinx.serialization.Serializable

/**
 * @param id Идентификатор опроса
 * @param serial Порядковый номер варианта ответа
 * @param content Текстовое содержимое вопроса
 * @param isMultipleChoiceAllowed Разрешен ли множественный выбор
 * @param answers Варианты ответов опроса
 */
@Serializable
data class QuestionDetailsDto (
    val id: String,
    val serial: Int,
    val content: String,
    val isMultipleChoiceAllowed: Boolean,
    val answers: List<QuestionAnswerDetailsDto>
)