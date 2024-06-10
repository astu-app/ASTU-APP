package org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation

import kotlinx.datetime.LocalDateTime

data class CreateSurvey (
    val questions: List<CreateQuestion>,
    val isAnonymous: Boolean,
    val resultsOpenBeforeClosing: Boolean,
    val voteUntil: LocalDateTime?,
)

