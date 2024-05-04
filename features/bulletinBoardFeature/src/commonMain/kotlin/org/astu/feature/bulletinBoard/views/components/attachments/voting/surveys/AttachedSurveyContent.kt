package org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.components.attachments.voting.VoteButton
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.PagedQuestions
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.QuestionContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.VotedQuestionContent

class AttachedSurveyContent(val id: Uuid, questions: List<QuestionContentBase>) : SurveyContentBase(questions) {
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
        return Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    }
}