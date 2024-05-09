package org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.RadioButtonsStateKeeper
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.SingleChoiceAnswerContent

class SingleChoiceQuestionContent (
    id: Uuid,
    text: String,
    answers: List<SingleChoiceAnswerContent>
) : UnvotedQuestionContentBase(id, text, answers) {

    init {
        val states = RadioButtonsStateKeeper()
        answers.forEach {
            val stateId = states.add(it)
            it.setSelectedStateParams(stateId, states)
        }
    }



    @Composable
    override fun Content(modifier: Modifier) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxWidth()
                .selectableGroup()
        ) {
            Text(text)
            answers.forEach {
                it.Content(it.getDefaultModifier())
            }
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    }
}