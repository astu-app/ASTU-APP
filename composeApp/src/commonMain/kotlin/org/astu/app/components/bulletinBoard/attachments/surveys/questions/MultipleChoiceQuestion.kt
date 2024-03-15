package org.astu.app.components.bulletinBoard.attachments.surveys.questions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.MultipleChoiceAnswer
import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.MultipleChoiceAnswerContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.MultipleChoiceQuestionContent

@Composable
fun MultipleChoiceQuestion(
    content: MultipleChoiceQuestionContent,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
    ) {
        Text(content.text)

        content.answers.forEach {
            MultipleChoiceAnswer(it as MultipleChoiceAnswerContent)
        }
    }
}