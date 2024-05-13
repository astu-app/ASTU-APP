package org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.viewModels.surveys.AttachedSurveyViewModel
import org.astu.feature.bulletinBoard.views.components.attachments.voting.VoteButton
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.PagedQuestions
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.QuestionContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.VotedQuestionContent
import org.astu.infrastructure.components.ActionFailedDialog

class AttachedSurveyContent(
    val id: Uuid,
    questions: List<QuestionContentBase>,
    val isVotedByUser: Boolean,
    val isOpen: Boolean,
) : SurveyContentBase(questions) {
    @Composable
    override fun Content(modifier: Modifier) {
        val viewModel = remember { AttachedSurveyViewModel(this, isVotedByUser) }

        val state by viewModel.state.collectAsState()
        when(state) {
            AttachedSurveyViewModel.State.NotVoted -> setVoteButtonStateToNotVoted(viewModel)
            AttachedSurveyViewModel.State.Voted -> setVoteButtonStateToVoted(viewModel)
            AttachedSurveyViewModel.State.VotesUploadingError -> showErrorDialog(viewModel)
            else -> { } // todo получение результатов опроса и вывод их на экран
        }

        Questions(viewModel, modifier)

        if (viewModel.showErrorDialog) {
            ActionFailedDialog(
                label = viewModel.errorDialogLabel,
                body = viewModel.errorDialogBody,
                onTryAgainRequest = {
                    hideErrorDialog(viewModel)
                    viewModel.vote()
                },
                onDismissRequest = {
                    hideErrorDialog(viewModel)
                    viewModel.resetViewModelState()
                }
            )
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    }



    @Composable
    private fun Questions(viewModel: AttachedSurveyViewModel, modifier: Modifier) {
        Column(modifier = modifier) {
            PagedQuestions(questions)

            if (questions[0] !is VotedQuestionContent) {
                VoteButton(
                    buttonContent = { Text(viewModel.voteButtonText) }, // todo индикатор загрузке при выгружке результатов опроса
                    enabled = viewModel.canVote() && !viewModel.mutableIsVotedByUser,
                    onClick = { viewModel.vote() },
                )
            }
        }
    }

    private fun setVoteButtonStateToNotVoted(viewModel: AttachedSurveyViewModel) {
        viewModel.voteButtonText = "Голосовать"
    }

    private fun setVoteButtonStateToVoted(viewModel: AttachedSurveyViewModel) {
        viewModel.voteButtonText = "Голос учтен. Спасибо."
    }

    private fun showErrorDialog(viewModel: AttachedSurveyViewModel) {
        viewModel.showErrorDialog = true
    }

    private fun hideErrorDialog(viewModel: AttachedSurveyViewModel) {
        viewModel.showErrorDialog = false
    }
}