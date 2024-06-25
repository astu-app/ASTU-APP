package org.astu.feature.bulletinBoard.viewModels.userGroups

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
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
import org.astu.feature.bulletinBoard.viewModels.humanization.ErrorCodesHumanization.humanize
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupToViewMappers.toView
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupsPresentationMapper.toShortUserGroupHierarchy
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.INode
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.UserGroupDetailsScreen

class UserGroupHierarchyViewModel(private val navigator: Navigator) : StateScreenModel<UserGroupHierarchyViewModel.State>(State.Loading) {
    sealed class State {
        data object Loading : State()
        data object LoadingDone : State()
        data object LoadingUserGroupsError : State()
        data object Deleting : State()
        data object DeletingError : State()
    }

    private val userGroupModel: UserGroupModel = UserGroupModel()
    var userGroups: SnapshotStateMap<Uuid, INode> = mutableStateMapOf()

    var pressOffset by mutableStateOf(DpOffset.Zero)
    var selectedUserGroupPosition: LayoutCoordinates? = null
    var showDropDown by mutableStateOf(false)
    var selectedUserGroupId by mutableStateOf(uuidFrom("00000000-0000-0000-0000-000000000000"))
    var selectedUserGroupName by mutableStateOf("Группа пользователей")
    var currentDensity by mutableStateOf(1f)

    val selectedUserGroupYPosition: Dp
        get() = selectedUserGroupPosition?.positionInRoot()?.y?.dp ?: 0.dp

    val hierarchyRootsForUserGroupSelection: MutableMap<Uuid, @Composable () -> Unit> = mutableMapOf()
    var selectedRootId: MutableState<Uuid?> = mutableStateOf(null)
    var selectedHierarchyRoot: MutableState<@Composable () -> Unit> = mutableStateOf({ })
    val isSelectUserGroupExpanded: MutableState<Boolean> = mutableStateOf(false)

    val rootUserGroupId: Uuid
        get() = selectedRootId.value ?: uuidFrom("00000000-0000-0000-0000-000000000000")

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
                hierarchyRootsForUserGroupSelection.clear()

                val userGroups = userGroupModel.getUserGroupHierarchy()
                if (userGroups.isContentValid) {
                    this@UserGroupHierarchyViewModel.userGroups.putAll(
                        userGroups.content!!.toShortUserGroupHierarchy(
                            onUserGroupClick =  {
                                val userGroupDetailsScreen = UserGroupDetailsScreen(it.id, it.name, rootUserGroupId) { navigator.pop() }
                                navigator.push(userGroupDetailsScreen)
                            },
                            onUserGroupLongPress = { userGroup, position, offset ->
                                selectedUserGroupId = userGroup.id
                                selectedUserGroupName = userGroup.name
                                selectedUserGroupPosition = try {
                                    position
                                } catch (e: Exception) {
                                    null
                                }
                                pressOffset = offset
                                showDropDown = true
                            },
                        )
                    )

                    hierarchyRootsForUserGroupSelection.putAll(
                        userGroups.content.roots.associate {
                            it.id to it.toView {
                                selectedRootId.value = it.id
                                selectedHierarchyRoot.value = hierarchyRootsForUserGroupSelection[selectedRootId.value] ?: { }
                                isSelectUserGroupExpanded.value = !isSelectUserGroupExpanded.value
                                Logger.d("${it.id}, ${it.name}", tag = "rootUserGroup")
                            }
                        }
                    )
                    selectedRootId = mutableStateOf(hierarchyRootsForUserGroupSelection.keys.firstOrNull())
                    selectedHierarchyRoot.value = hierarchyRootsForUserGroupSelection[selectedRootId.value] ?: { }

                    mutableState.value = State.LoadingDone
                    return@launch
                } else {
                    constructUserGroupsLoadingErrorDialogContent(userGroups.error)
                    mutableState.value = State.LoadingUserGroupsError
                }

            } catch (e: Exception) {
                Logger.e(this::class.simpleName!! , e)
                constructUserGroupsLoadingErrorDialogContent()
                mutableState.value = State.LoadingUserGroupsError
            }
        }
    }

    fun deleteUserGroup(id: Uuid) {
        screenModelScope.launch {
            try {
                mutableState.value = State.Deleting

                val error = userGroupModel.delete(id, rootUserGroupId)
                if (error == null) {
                    mutableState.value = State.LoadingDone
                    loadUserGroups()
                    return@launch
                } else {
                    constructUserGroupsDeletingErrorDialogContent(id, error)
                    mutableState.value = State.LoadingUserGroupsError
                }

            } catch (e: Exception) {
                Logger.e(this::class.simpleName!! , e)
                constructUserGroupsDeletingErrorDialogContent(id)
                mutableState.value = State.LoadingUserGroupsError
            }
        }
    }



    private fun constructUserGroupsLoadingErrorDialogContent(error: GetUserHierarchyErrors? = null) {
        errorDialogBody = error.humanize()
        onErrorDialogTryAgainRequest = {
            loadUserGroups()
        }
        onErrorDialogDismissRequest = {
            mutableState.value = State.LoadingDone
        }
    }

    private fun constructUserGroupsDeletingErrorDialogContent(userGroupId: Uuid, error: DeleteUserGroupErrors? = null) {
        errorDialogBody = error.humanize()
        onErrorDialogTryAgainRequest = {
            deleteUserGroup(userGroupId)
        }
        onErrorDialogDismissRequest = {
            mutableState.value = State.LoadingDone
        }
    }
}