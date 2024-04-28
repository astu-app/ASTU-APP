package org.astu.feature.bulletinBoard.views.components.attachments.surveys.questions

import androidx.compose.runtime.Composable
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.questions.models.QuestionContentBase

@Composable
fun Question(question: QuestionContentBase) {
    question.Content(question.getDefaultModifier())
}