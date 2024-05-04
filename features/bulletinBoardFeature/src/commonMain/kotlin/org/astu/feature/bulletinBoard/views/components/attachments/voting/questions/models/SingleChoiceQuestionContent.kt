package org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.RadioButtonsStateKeeper
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.SingleChoiceAnswerContent

class SingleChoiceQuestionContent (
    text: String,
    answers: List<SingleChoiceAnswerContent>
) : QuestionContentBase(text, answers) {
    @Composable
    override fun Content(modifier: Modifier) {
        val states = remember { RadioButtonsStateKeeper() }
        answers
            .forEach {
                val content = it as SingleChoiceAnswerContent
                val id = states.add(content)
                content.setSelectedStateParams(id, states)
            }

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