package org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid

class SingleChoiceAnswerContent(
    id: Uuid,
    text: String,
    canVote: MutableState<Boolean>,
) : UnvotedAnswerContentBase(id, text, canVote) {
    private var stateId: MutableState<Int?> = mutableStateOf(null)
    private var stateKeeper: MutableState<RadioButtonsStateKeeper?> = mutableStateOf(null)

    fun setSelectedStateParams(stateId: Int, stateKeeper: RadioButtonsStateKeeper) {
        this.stateId.value = stateId
        this.stateKeeper.value = stateKeeper
    }

    override fun isSelected(): Boolean {
        return stateKeeper.value?.getSelected() == stateId.value
    }

    @Composable
    override fun Content(modifier: Modifier) {
        if (stateId.value == null || stateKeeper.value == null)
            return

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            val selected by mutableStateOf(isSelected())

            RadioButton(
                selected = selected,
                onClick = {
                    stateKeeper.value!!.selectedStateId = stateId.value!!
                },
                enabled = canVote.value,
            )
            Text(
                text = stateKeeper.value!!.getState(stateId.value!!)?.text ?: "Не удалось получить текст ответа",
            )
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(vertical = 4.dp)
    }
}