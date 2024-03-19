package org.astu.app.components.bulletinBoard.attachments.surveys.questions.models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.SingleChoiceAnswer
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.RadioButtonsStateKeeper
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.SingleChoiceAnswerContent

class SingleChoiceQuestionContent (
    text: String,
    answers: List<SingleChoiceAnswerContent>
) : QuestionContentBase(text, answers) {
    @Composable
    override fun Content(modifier: Modifier) {
        val states = remember { RadioButtonsStateKeeper() }
        answers.forEach {
            val answerContent = SingleChoiceAnswerContent(it.text)
            states.add(answerContent)
        }
        val stateIds = states.getIds()

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxWidth()
                .selectableGroup()
        ) {
            Text(text)
            stateIds.forEach {
                SingleChoiceAnswer(it, states)
            }
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    }
}