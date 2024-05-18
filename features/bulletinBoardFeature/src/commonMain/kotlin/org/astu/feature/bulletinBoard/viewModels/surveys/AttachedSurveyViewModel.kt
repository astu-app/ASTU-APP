package org.astu.feature.bulletinBoard.viewModels.surveys

import androidx.compose.runtime.*
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.models.SurveyModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.VoteInSurveyErrors
import org.astu.feature.bulletinBoard.viewModels.humanization.ErrorCodesHumanization.humanize
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.AttachedSurveyContent
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.SurveyMappers.getVotes
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.SurveyMappers.toModel

class AttachedSurveyViewModel(
    private val survey: AttachedSurveyContent,
    private val isVotedByUser: Boolean,
) : StateScreenModel<AttachedSurveyViewModel.State>(
    State.NotVoted
) {
    sealed class State {
        data object NotVoted: State()
        data object VotesUploading: State()
        data object Voted: State()
        data object VotesUploadingError: State()
    }

    private val surveyModel: SurveyModel = SurveyModel()

    var mutableIsVotedByUser by mutableStateOf(isVotedByUser)

    var voteButtonText by mutableStateOf("")

    private val unexpectedErrorTitle: String = "Ошибка"
    private val unexpectedErrorBody: String = "Непредвиденная ошибка при голсовании в опросе. Повторите попытку"
    var errorDialogLabel by mutableStateOf(unexpectedErrorTitle)
    var errorDialogBody by mutableStateOf(unexpectedErrorBody)
    var showErrorDialog by mutableStateOf(false)

    init {
        mutableState.value = if (isVotedByUser) State.Voted else State.NotVoted
    }

    fun canVote(): Boolean {
        return survey.getVotes().isValid();
    }

    fun vote() {
        mutableState.value = State.VotesUploading
        screenModelScope.launch {
            try {
                val error = surveyModel.vote(survey.getVotes().toModel())

                if (error != null) {
                    constructVoteErrorDialog()
                    mutableState.value = State.VotesUploadingError
                    return@launch
                }

                mutableIsVotedByUser = true
                mutableState.value = State.Voted

            } catch (e: Exception) {
                constructVoteErrorDialog()
                mutableState.value = State.VotesUploadingError
            }
        }
    }

    fun resetViewModelState() {
        mutableState.value = AttachedSurveyViewModel.State.NotVoted
    }



    private fun constructVoteErrorDialog(error:  VoteInSurveyErrors? = null) {
        errorDialogBody = error.humanize()
    }
}