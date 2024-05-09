package org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid

class MultipleChoiceAnswerContent(
    id: Uuid,
    text: String,
    canVote: MutableState<Boolean>,
    var selected: MutableState<Boolean> = mutableStateOf(false)
) : UnvotedAnswerContentBase(id, text, canVote)
{
    override fun isSelected(): Boolean {
        return selected.value
    }

    @Composable
    override fun Content(modifier: Modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Checkbox(
                checked = selected.value,
                onCheckedChange = {
                    selected.value = !selected.value
                },
                enabled = canVote.value,
            )
            Text(text)
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 4.dp)
    }

}