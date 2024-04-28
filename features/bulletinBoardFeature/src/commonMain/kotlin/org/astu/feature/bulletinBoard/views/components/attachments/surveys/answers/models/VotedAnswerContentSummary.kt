package org.astu.feature.bulletinBoard.views.components.attachments.surveys.answers.models

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class VotedAnswerContentSummary(text: String, voterPercent: Int) : VotedAnswerContentBase(text, voterPercent) {
    @Composable
    override fun Content(modifier: Modifier) {
        DrawVotedContentSummary(modifier)
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp)
    }
}