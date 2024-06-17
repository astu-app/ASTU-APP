package org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.viewModels.humanization.humanizeDateTime
import org.astu.feature.bulletinBoard.viewModels.surveys.AttachedSurveyViewModel
import org.astu.feature.bulletinBoard.views.components.attachments.voting.VoteButton
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.PagedQuestions
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.ClosedResultsQuestionContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.QuestionContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.VotedQuestionContent
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary
import org.astu.feature.bulletinBoard.views.entities.users.UserToViewMappers.toViews
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.dropdown.DropDown
import org.astu.infrastructure.theme.CurrentColorScheme

class AttachedSurveyContent(
    id: Uuid,
    questions: List<QuestionContentBase>,
    voters: List<UserSummary>,
    showVoters: Boolean,
    val isVotedByUser: Boolean,
    val isOpen: Boolean,
    val showResults: Boolean,
    val isAnonymous: Boolean,
    val autoClosingAt: LocalDateTime?
) : SurveyContentBase(id, voters, showVoters, questions) {
    @Composable
    override fun Content(modifier: Modifier) {
        val viewModel = remember { AttachedSurveyViewModel(this, isVotedByUser, !isOpen, !showResults) }

        val state by viewModel.state.collectAsState()
        when(state) {
            AttachedSurveyViewModel.State.NotVoted -> setVoteButtonStateToNotVoted(viewModel)
            AttachedSurveyViewModel.State.VoteUnavailable -> setVoteButtonStateToVoted(viewModel)
            AttachedSurveyViewModel.State.VotesUploadingError -> showErrorDialog(viewModel)
            AttachedSurveyViewModel.State.VotesUploading -> { } // todo получение результатов опроса и вывод их на экран
        }

        if (!isOpen) {
            Text("Опрос закрыт", modifier = Modifier.padding(bottom = 8.dp), color = CurrentColorScheme.outline)
        }

        if (!showResults) {
            Text("Результаты недоступны", modifier = Modifier.padding(bottom = 8.dp), color = CurrentColorScheme.outline)
        } else if (isAnonymous) {
            Text("Опрос анонимный", modifier = Modifier.padding(bottom = 8.dp), color = CurrentColorScheme.outline)
        }

        if (autoClosingAt != null) {
            Text("Будет закрыт ${humanizeDateTime(autoClosingAt)}", modifier = Modifier.padding(bottom = 8.dp), color = CurrentColorScheme.outline)
        }

        Questions(viewModel, modifier)

        if (showVoters) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp))
            val voterViews = remember { voters.toViews() }
            DropDown(voterViews) {
                Text(
                    text = "Проголосовавшие",
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                )
            }
        }

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

            if (questions[0] !is VotedQuestionContent && questions[0] !is ClosedResultsQuestionContent) {
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