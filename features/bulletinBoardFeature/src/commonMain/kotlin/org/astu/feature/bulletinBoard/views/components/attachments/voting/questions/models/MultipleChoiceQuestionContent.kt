package org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.MultipleChoiceAnswerContent

class MultipleChoiceQuestionContent(
    text: String,
    answers: List<MultipleChoiceAnswerContent>
) : QuestionContentBase(text, answers) {
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