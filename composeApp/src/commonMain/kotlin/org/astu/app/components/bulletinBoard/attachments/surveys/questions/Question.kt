package org.astu.app.components.bulletinBoard.attachments.surveys.questions

import androidx.compose.runtime.Composable
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.MultipleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.QuestionContentBase
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.SingleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.VotedQuestionContent

@Composable
fun Question(question: QuestionContentBase) {
    when (question) {
        is MultipleChoiceQuestionContent -> {
            MultipleChoiceQuestion(question)
        }

        is SingleChoiceQuestionContent -> {
            SingleChoiceQuestion(question)
        }

        is VotedQuestionContent -> {
            VotedQuestion(question)
        }
    }
}