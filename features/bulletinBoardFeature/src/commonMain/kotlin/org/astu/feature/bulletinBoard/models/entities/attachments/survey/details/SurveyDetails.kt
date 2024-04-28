package org.astu.feature.bulletinBoard.models.entities.attachments.survey.details

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime

data class SurveyDetails(
    val id: Uuid,
    val isOpen: Boolean,
    val isAnonymous: Boolean,
    val votersAmount: Int,
    val autoClosingAt: LocalDateTime?,
    val voteFinishedAt: LocalDateTime?,
    val questions: List<QuestionDetails>,
)