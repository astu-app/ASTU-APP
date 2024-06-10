package org.astu.feature.bulletinBoard.viewModels.userGroups

import cafe.adriel.voyager.core.model.StateScreenModel

class UserGroupListViewModel : StateScreenModel<UserGroupListViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object LoadingUserGroupsError : State()
        data object Deleting : State()
        data object DeletingError : State()
    }

//    private val userGroupModel: UserGroupModel = UserGroupModel()
//    var userGroups: SnapshotStateList<UserGroupSummaryContent> = mutableStateListOf()
//
//    var pressOffset by mutableStateOf(DpOffset.Zero)
//    var showDropDown by mutableStateOf(false)
//    var selectedUserGroupId by mutableStateOf(uuidFrom("00000000-0000-0000-0000-000000000000"))
//    var selectedUserGroupName by mutableStateOf("Группа пользователей")
//    var currentDensity by mutableStateOf(1f)
//
//    private val unexpectedErrorTitle: String = "Ошибка"
//    private val unexpectedErrorBody: String = "Неожиданная ошибка. Повторите попытку"
//    var errorDialogLabel by mutableStateOf(unexpectedErrorTitle)
//    var errorDialogBody by mutableStateOf(unexpectedErrorBody)
//    var showErrorDialog by mutableStateOf(false)
//    var onErrorDialogTryAgainRequest by mutableStateOf({ })
//    var onErrorDialogDismissRequest by mutableStateOf({ })
//
//    init {
//        loadUserGroups()
//    }
//
//    fun loadUserGroups() {
//        screenModelScope.launch {
//            try {
//                mutableState.value = State.Loading
//                userGroups.clear()
//
//                val userGroups = userGroupModel.getUserGroupList()
//                if (userGroups.isContentValid) {
//                    this@UserGroupListViewModel.userGroups.addAll(
//                        userGroups.content!!.toPresentations()
//                    )
//                    mutableState.value = State.LoadingDone
//                    return@launch
//                } else {
//                    constructUserGroupsLoadingErrorDialogContent(userGroups.error)
//                    mutableState.value = State.LoadingUserGroupsError
//                }
//
//            } catch (e: Exception) {
//                Logger.e(this::class.simpleName!! , e)
//                constructUserGroupsLoadingErrorDialogContent()
//                mutableState.value = State.LoadingUserGroupsError
//            }
//        }
//    }
//
//    fun deleteUserGroup(id: Uuid) {
//        screenModelScope.launch {
//            try {
//                mutableState.value = State.Deleting
//
//                val error = userGroupModel.delete(id)
//                if (error == null) {
//                    mutableState.value = State.LoadingDone
//                    return@launch
//                } else {
//                    constructUserGroupsDeletingErrorDialogContent(id, error)
//                    mutableState.value = State.LoadingUserGroupsError
//                }
//
//            } catch (e: Exception) {
//                Logger.e(this::class.simpleName!! , e)
//                constructUserGroupsDeletingErrorDialogContent(id)
//                mutableState.value = State.LoadingUserGroupsError
//            }
//        }
//    }
//
//
//
//    private fun constructUserGroupsLoadingErrorDialogContent(error: GetUserListErrors? = null) {
//        errorDialogBody = error.humanize()
//        onErrorDialogTryAgainRequest = {
//            loadUserGroups()
//            showErrorDialog = false
//        }
//        onErrorDialogDismissRequest = {
//            showErrorDialog = false
//        }
//    }
//
//    private fun constructUserGroupsDeletingErrorDialogContent(userGroupId: Uuid, error: DeleteUserGroupErrors? = null) {
//        errorDialogBody = error.humanize()
//        onErrorDialogTryAgainRequest = {
//            deleteUserGroup(userGroupId)
//            showErrorDialog = false
//        }
//        onErrorDialogDismissRequest = {
//            showErrorDialog = false
//        }
//    }
}