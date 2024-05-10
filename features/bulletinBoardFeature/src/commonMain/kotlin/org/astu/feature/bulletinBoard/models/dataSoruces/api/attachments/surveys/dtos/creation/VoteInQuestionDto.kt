package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.creation

import kotlinx.serialization.Serializable

/**
 * DTO для голосования в вопросе опроса
 * @param questionId Идентификатор вопроса
 * @param answerIds Массив идентификаторов вариантов ответов
 */
@Serializable
data class VoteInQuestionDto (
    val questionId: String,
    val answerIds: List<String>
)