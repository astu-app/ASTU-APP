package org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import kotlinx.datetime.LocalDateTime

data class CreateSurvey (
    val questions: List<CreateQuestion>,
    val isAnonymous: Boolean,
    val resultsOpenBeforeClosing: Boolean,
    val voteUntil: LocalDateTime?,
) {
    var rootUserGroupId: Uuid = uuidFrom("00000000-0000-0000-0000-000000000000")
}

