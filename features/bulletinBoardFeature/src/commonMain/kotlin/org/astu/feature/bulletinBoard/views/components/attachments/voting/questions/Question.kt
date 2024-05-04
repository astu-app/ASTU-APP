package org.astu.feature.bulletinBoard.views.components.attachments.voting.questions

import androidx.compose.runtime.Composable
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.QuestionContentBase

@Composable
fun Question(question: QuestionContentBase) {
    question.Content(question.getDefaultModifier())
}