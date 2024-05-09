package org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.benasher44.uuid.Uuid

data class QuestionVotes (
    val questionId: Uuid,
    val isMultipleChoiceAllowed: Boolean,
    val answersVotes: SnapshotStateList<Uuid>
) {
    fun isValid(): Boolean {
        if (answersVotes.isEmpty())
            return false;

        if (isMultipleChoiceAllowed)
            return true; // разрешено любоеколичество вариантов ответов

        // если множественный выбор не разрешен, может быть выборан только один вариант ответа
        return answersVotes.size == 1
    }
}