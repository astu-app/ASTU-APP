package org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.astu.infrastructure.components.dropdown.DropDown

class VotedAnswerContentDetails(
    id: Uuid,
    text: String,
    voterPercent: Int,
    private val voters: List<@Composable () -> Unit>?
) : VotedAnswerContentBase(id, text, voterPercent) {
    @Composable
    override fun Content(modifier: Modifier) {
        DrawVotedContentSummary(modifier)
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .wrapContentHeight()
            .padding(4.dp)
    }

    @Composable
    override fun DrawVotedContentSummary(modifier: Modifier) {
        if (voters == null) {
            super.DrawVotedContentSummary(modifier)

        } else {
            val dropdownExpanded = remember { mutableStateOf(false) }
            DropDown(items = voters, isExpanded = dropdownExpanded) {
                super.DrawVotedContentSummary(modifier)
            }
        }
    }
}