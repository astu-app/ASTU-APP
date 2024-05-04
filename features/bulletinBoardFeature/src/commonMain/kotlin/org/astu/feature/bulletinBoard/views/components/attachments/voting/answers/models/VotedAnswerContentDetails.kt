package org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.components.TreeDropDown
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.INode

class VotedAnswerContentDetails(
    id: Uuid,
    text: String,
    voterPercent: Int,
    private val votersRootNode: INode?
) : VotedAnswerContentBase(id, text, voterPercent) {
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