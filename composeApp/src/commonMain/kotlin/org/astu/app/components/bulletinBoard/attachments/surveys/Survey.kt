package org.astu.app.components.bulletinBoard.attachments.surveys

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.app.components.bulletinBoard.attachments.surveys.common.models.SurveyContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.MultipleChoiceQuestion
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.SingleChoiceQuestion
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.VotedQuestion
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.MultipleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.SingleChoiceQuestionContent
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.VotedQuestionContent

@Composable
fun Survey(
    content: SurveyContent,
    modifier: Modifier = Modifier
        .fillMaxSize()
) {
    Column(modifier = modifier) {
        content.questions.forEach {
            when (it) {
                is MultipleChoiceQuestionContent -> {
                    MultipleChoiceQuestion(it)
                }

                is SingleChoiceQuestionContent -> {
                    SingleChoiceQuestion(it)
                }

                is VotedQuestionContent -> {
                    VotedQuestion(it)
                }
            }
        }
        VoteButton()
    }
}