package org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.QuestionVotes

data class SurveyVotes (
    val surveyId: Uuid,
    val questionVotes: SnapshotStateList<QuestionVotes>
) {
    fun isValid(): Boolean {
        return questionVotes.all { it.isValid() }
    }
}

