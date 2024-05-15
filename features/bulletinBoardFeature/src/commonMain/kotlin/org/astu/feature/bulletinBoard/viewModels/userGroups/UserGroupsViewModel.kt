package org.astu.feature.bulletinBoard.viewModels.userGroups

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.DpOffset
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import co.touchlab.kermit.Logger
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.models.UserGroupModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.DeleteUserGroupErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyErrors
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupsPresentationMapper.toShortUserGroupHierarchy
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.INode
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.UserGroupDetailsScreen

class UserGroupsViewModel(private val navigator: Navigator) : StateScreenModel<UserGroupsViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object LoadingUserGroupsError : State()
        data object Deleting : State()
        data object DeletingError : State()
    }

    private val userGroupModel: UserGroupModel = UserGroupModel()
    var userGroups: SnapshotStateList<INode> = mutableStateListOf()

    var pressOffset by mutableStateOf(DpOffset.Zero)
    var showDropDown by mutableStateOf(false)
    var selectedUserGroupId by mutableStateOf(uuidFrom("00000000-0000-0000-0000-000000000000"))
    var selectedUserGroupName by mutableStateOf("Группа пользователей")
    var currentDensity by mutableStateOf(1f)

    private val unexpectedErrorTitle: String = "Ошибка"
    private val unexpectedErrorBody: String = "Неожиданная ошибка. Повторите попытку"
    var errorDialogLabel by mutableStateOf(unexpectedErrorTitle)
    var errorDialogBody by mutableStateOf(unexpectedErrorBody)
    var showErrorDialog by mutableStateOf(false)
    var onErrorDialogTryAgainRequest by mutableStateOf({ })
    var onErrorDialogDismissRequest by mutableStateOf({ })

    init {
        loadUserGroups()
    }

    fun loadUserGroups() {
        screenModelScope.launch {
            try {
                mutableState.value = State.Loading
                userGroups.clear()

                val userGroups = userGroupModel.getUserGroupList()
                if (userGroups.isContentValid) {
                    this@UserGroupsViewModel.userGroups.addAll(
                        userGroups.content!!.toShortUserGroupHierarchy(
                            onUserGroupClick =  {
                                val userGroupDetailsScreen = UserGroupDetailsScreen(it.id, it.name) { navigator.pop() }
                                navigator.push(userGroupDetailsScreen)
                            },
                            onUserGroupLongPress = { userGroup, offset ->
                                selectedUserGroupId = userGroup.id
                                selectedUserGroupName = userGroup.name
                                pressOffset = offset
                                showDropDown = true
                            }
                        )
                    )
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

    fun deleteUserGroup(id: Uuid) {
        screenModelScope.launch {
            try {
                mutableState.value = State.Deleting

                val error = userGroupModel.delete(id)
                if (error == null) {
                    mutableState.value = State.LoadingDone
                    return@launch
                } else {
                    constructUserGroupsDeletingErrorDialogContent(id, error)
                    mutableState.value = State.LoadingUserGroupsError
                }

            } catch (e: Exception) {
                Logger.e("UserGroupsViewModel", e)
                constructUserGroupsDeletingErrorDialogContent(id)
                mutableState.value = State.LoadingUserGroupsError
            }
        }
    }



    private fun constructUserGroupsLoadingErrorDialogContent(error: GetUserHierarchyErrors? = null) {
        errorDialogBody = when (error) {
            GetUserHierarchyErrors.GetUsergroupHierarchyForbidden -> "У вас недостаточно прав для просмотра списка групп пользователей"
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

    private fun constructUserGroupsDeletingErrorDialogContent(userGroupId: Uuid, error: DeleteUserGroupErrors? = null) {
        errorDialogBody = when (error) {
            DeleteUserGroupErrors.UsergroupDeletionForbidden -> "У вас отсутствуют права на удаление группы пользователей"
            DeleteUserGroupErrors.UserGroupDoesNotExist -> "Группа пользователей не найдена"
            else -> unexpectedErrorBody
        }
        onErrorDialogTryAgainRequest = {
            deleteUserGroup(userGroupId)
            showErrorDialog = false
        }
        onErrorDialogDismissRequest = {
            showErrorDialog = false
        }
    }
}