package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.details

import kotlinx.serialization.Serializable
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.dtos.UserSummaryDto

/**
 * @param id Идентификатор варианта ответа
 * @param serial Порядковый номер варианта ответа
 * @param content Текстовое содержимое варианта ответа
 * @param voters Список проголосовавших за вариант ответа пользователей. Пустой, если вариант ответа относится к анонимному опросу
 * @param votersAmount Количество пользователей, проголосовавших за вариант ответа
  */
@Serializable
data class QuestionAnswerDetailsDto (
    val id: String,
    val serial: Int,
    val content: String,
    val voters: List<UserSummaryDto>,
    val votersAmount: Int,
)