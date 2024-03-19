package org.astu.app.components.bulletinBoard.attachments.surveys.common.models

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentType
import org.astu.app.components.bulletinBoard.attachments.surveys.PagedQuestions
import org.astu.app.components.bulletinBoard.attachments.surveys.VoteButton
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.QuestionContentBase
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.VotedQuestionContent

class SurveyContent(private val questions: List<QuestionContentBase>) : AttachmentBase(AttachmentType.SURVEY) {
    @Composable
    override fun Content(modifier: Modifier) {
        Column(modifier = modifier) {
            PagedQuestions(questions)

            if (questions[0] !is VotedQuestionContent) {
                VoteButton()
            }
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier.fillMaxSize()
    }
}