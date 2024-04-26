package org.astu.app.dataSources.bulletInBoard.attachments.surveys.dtos

import kotlinx.serialization.Serializable

/**
 * @param id Идентификатор варианта ответа
 * @param serial Порядковый номер варианта ответа
 * @param content Текстовое содержимое варианта ответа
 * @param votersAmount Количество пользователей, проголосовавших за вариант ответа
  */
@Serializable
data class QuestionAnswerDetailsDto (
    val id: String,
    val serial: Int,
    val content: String,
    val votersAmount: Int,
)