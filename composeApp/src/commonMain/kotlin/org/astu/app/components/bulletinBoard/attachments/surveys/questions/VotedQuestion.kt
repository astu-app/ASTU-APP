package org.astu.app.components.bulletinBoard.attachments.surveys.questions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.VotedAnswer
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.VotedAnswerContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.VotedQuestionContent

@Composable
fun VotedQuestion(
    content: VotedQuestionContent,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(content.text)

        content.answers.forEach {
            VotedAnswer(it as VotedAnswerContent)
        }
    }
}