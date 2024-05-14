package org.astu.feature.bulletinBoard.viewModels.userGroups

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import co.touchlab.kermit.Logger
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.models.UserGroupsModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyResponses
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.INode
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.mappers.UserGroupsPresentationMapper.toShortUserGroupHierarchy
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.UserGroupDetailsScreen

class UserGroupsViewModel(private val navigator: Navigator) : StateScreenModel<UserGroupsViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object LoadingUserGroupsError : State()
    }

    private val userGroupModel: UserGroupsModel = UserGroupsModel()
    var userGroups: SnapshotStateList<INode> = mutableStateListOf()

    private val unexpectedErrorTitle: String = "Ошибка"
    private val unexpectedErrorBody: String = "Неожиданная ошибка. Повторите попытку"
    var errorDialogLabel by mutableStateOf(unexpectedErrorTitle)
    var errorDialogBody by mutableStateOf(unexpectedErrorBody)
    var showErrorDialog by mutableStateOf(false)
    var onErrorDialogTryAgainRequest by mutableStateOf({ })
    var onErrorDialogDismissRequest by mutableStateOf({ })

    init {
        mutableState.value = State.Loading
        loadUserGroups()
    }

    fun loadUserGroups() {
        screenModelScope.launch {
            try {
                userGroups.clear()

                val userGroups = userGroupModel.getUserGroupList()
                if (userGroups.isContentValid) {
                    this@UserGroupsViewModel.userGroups.addAll(userGroups.content!!.toShortUserGroupHierarchy {
                        val userGroupDetailsScreen = UserGroupDetailsScreen(it.id) { navigator.pop() }
                        navigator.push(userGroupDetailsScreen)
                    })
                    mutableState.value = State.LoadingDone
                    return@launch
                } else {
                    constructUserGroupsLoadingErrorDialogContent(userGroups.error)
                    mutableState.value = State.LoadingUserGroupsError
                }

            } catch (e: Exception) {
                Logger.e("UserGroupsViewModel", e)
                constructUserGroupsLoadingErrorDialogContent()
                mutableState.value = State.LoadingUserGroupsError
            }
        }
    }



    private fun constructUserGroupsLoadingErrorDialogContent(error: GetUserHierarchyResponses? = null) {
        errorDialogBody = when (error) {
            GetUserHierarchyResponses.GetUsergroupHierarchyForbidden -> "У вас недостаточно прав для просмотра списка групп пользователей"
            else -> unexpectedErrorBody
        }
        onErrorDialogTryAgainRequest = {
            loadUserGroups()
            showErrorDialog = false
        }
        onErrorDialogDismissRequest = {
            showErrorDialog = false
        }
    }
}