package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.creation

import kotlinx.serialization.Serializable

/**
 * DTO для создания вопроса
 * @param serial Порядковый номер вопроса в списке вопросов
 * @param content Содержимое вопроса
 * @param isMultipleChoiceAllowed Доступен ли выбор нескольких вариантов ответов
 * @param answers Варианты ответов
 */
@Serializable
data class CreateQuestionDto (
    val serial: Int,
    val content: String,
    val isMultipleChoiceAllowed: Boolean,
    val answers: List<CreateAnswerDto>
)