package org.astu.app.components.bulletinBoard.attachments.surveys.questions

import androidx.compose.runtime.Composable
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.QuestionContentBase

@Composable
fun Question(question: QuestionContentBase) {
    question.Content(question.getDefaultModifier())
}