package org.astu.feature.bulletinBoard.models.dataSoruces.api.surveys.dtos

import kotlinx.serialization.Serializable

/**
 * DTO для голосования в опросе
 * @param surveyId идентификатор опроса
 * @param questionVotes голоса за варианты ответов в каждом вопросе опроса
 */
@Serializable
data class VoteInSurveyDto (
    val surveyId: String,
    val questionVotes: List<VoteInQuestionDto>
)

