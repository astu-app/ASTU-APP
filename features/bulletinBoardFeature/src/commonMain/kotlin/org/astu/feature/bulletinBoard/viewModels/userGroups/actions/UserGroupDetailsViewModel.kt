package org.astu.feature.bulletinBoard.viewModels.userGroups.actions

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.benasher44.uuid.Uuid
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.models.UserGroupModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUsergroupDetailsErrors
import org.astu.feature.bulletinBoard.viewModels.humanization.ErrorCodesHumanization.humanize
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupDetailsContent
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupsPresentationMapper.toPresentation

class UserGroupDetailsViewModel(
    private val id: Uuid,
    private val onReturn: () -> Unit,
) : StateScreenModel<UserGroupDetailsViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object LoadingError : State()
    }

    private val userGroupModel = UserGroupModel()
    val content: MutableState<UserGroupDetailsContent?> = mutableStateOf(null)

    private val unexpectedErrorTitle: String = "Ошибка"
    private val unexpectedErrorBody: String = "Неожиданная ошибка. Повторите попытку"
    var errorDialogLabel by mutableStateOf(unexpectedErrorTitle)
    var errorDialogBody by mutableStateOf(unexpectedErrorBody)
    var showErrorDialog by mutableStateOf(false)
    var onErrorDialogTryAgainRequest by mutableStateOf({ })
    var onErrorDialogDismissRequest by mutableStateOf({ })

    init {
        loadUserGroupDetails()
    }



    fun loadUserGroupDetails() {
        screenModelScope.launch {
            try {
                mutableState.value = State.Loading

                val details = userGroupModel.getDetails(id)
                if (!details.isContentValid) {
                    constructLoadingUserGroupDetailsErrorDialogContent(details.error)
                    mutableState.value = State.LoadingError
                    return@launch
                } else {
                    content.value = details.content!!.toPresentation()
                    mutableState.value = State.LoadingDone
                }

            } catch (e: Exception) {
                constructLoadingUserGroupDetailsErrorDialogContent()
                mutableState.value = State.LoadingError
                return@launch
            }
        }
    }



    private fun constructLoadingUserGroupDetailsErrorDialogContent(error: GetUsergroupDetailsErrors? = null) {
        errorDialogBody = error.humanize()

        onErrorDialogTryAgainRequest = {
            loadUserGroupDetails()
            showErrorDialog = false
        }
        onErrorDialogDismissRequest = {
            onReturn.invoke()
            showErrorDialog = false
        }
    }
}