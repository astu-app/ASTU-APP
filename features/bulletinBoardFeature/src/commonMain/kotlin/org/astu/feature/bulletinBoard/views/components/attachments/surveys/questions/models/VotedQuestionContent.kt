package org.astu.feature.bulletinBoard.views.components.attachments.surveys.questions.models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.answers.models.VotedAnswerContentBase

class VotedQuestionContent(
    text: String,
    answers: List<VotedAnswerContentBase>
) : QuestionContentBase(text, answers) {
    @Composable
    override fun Content(modifier: Modifier) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(text)

            answers.forEach {answer ->
                val votedAnswer = answer as VotedAnswerContentBase
                votedAnswer.Content(votedAnswer.getDefaultModifier())
            }
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    }
}