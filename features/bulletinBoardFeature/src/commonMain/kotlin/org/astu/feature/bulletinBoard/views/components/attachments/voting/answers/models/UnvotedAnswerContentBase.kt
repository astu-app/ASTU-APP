package org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models

import androidx.compose.runtime.MutableState
import com.benasher44.uuid.Uuid

abstract class UnvotedAnswerContentBase(
    id: Uuid,
    text: String,
    var canVote: MutableState<Boolean>,
) : AnswerContentBase(id, text) {
    abstract fun isSelected(): Boolean
}