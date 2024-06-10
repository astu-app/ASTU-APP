package org.astu.feature.bulletinBoard.models.entities.attachments.survey.details

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.models.entities.audience.User

data class SurveyDetails(
    val id: Uuid,
    val isVotedByUser: Boolean,
    val isOpen: Boolean,
    val isAnonymous: Boolean,
    val resultsOpenBeforeClosing: Boolean,
    val voters: List<User>,
    val votersAmount: Int,
    val autoClosingAt: LocalDateTime?,
    val voteFinishedAt: LocalDateTime?,
    val questions: List<QuestionDetails>,
) {
    val showResults: Boolean
        get() = !isOpen || (isOpen && resultsOpenBeforeClosing)
}