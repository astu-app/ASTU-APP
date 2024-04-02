package org.astu.app.components.bulletinBoard.attachments.surveys.answers.models

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.app.components.TreeDropDown
import org.astu.app.entities.bulletInBoard.audienceGraph.INode

class VotedAnswerContentDetails(
    text: String,
    voterPercent: Int,
    private val votersRootNode: INode?
) : VotedAnswerContentBase(text, voterPercent) {
    @Composable
    override fun Content(modifier: Modifier) {
        if (votersRootNode == null) {
            DrawVotedContentSummary(modifier)
        } else {
            TreeDropDown(votersRootNode, levelIndent = 20.dp) {
                DrawVotedContentSummary(modifier.fillMaxWidth(0.9f))
            }
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .wrapContentHeight()
            .padding(4.dp)
    }
}