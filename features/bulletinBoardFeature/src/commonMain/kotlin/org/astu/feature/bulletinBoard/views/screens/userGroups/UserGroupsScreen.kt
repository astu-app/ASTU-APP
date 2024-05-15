package org.astu.feature.bulletinBoard.views.screens.userGroups

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.feature.bulletinBoard.viewModels.userGroups.UserGroupsViewModel
import org.astu.feature.bulletinBoard.views.components.announcements.common.DisplayUserGroupHierarchySection
import org.astu.feature.bulletinBoard.views.components.userGroups.dropdownMenuContent.UserGroupSummaryDropDownMenuContent
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.CreateUserGroupScreen
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.EditUserGroupScreen
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.UserGroupDetailsScreen
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

// todo контекстное меню дял каждой группы пользователей
class UserGroupsScreen(private val onReturn: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { UserGroupsViewModel(navigator) }
        viewModel.currentDensity = LocalDensity.current.density

        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Группы пользователей") },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val createUserGroupScreen = CreateUserGroupScreen { navigator.pop() }
                        navigator.push(createUserGroupScreen)
                    },
                    containerColor = CurrentColorScheme.tertiaryContainer,
                ) {
                    Icon(Icons.Outlined.Edit, null)
                }
            }
        ) {
            val state by viewModel.state.collectAsState()
            when (state) {
                UserGroupsViewModel.State.Loading -> Loading()
                UserGroupsViewModel.State.LoadingDone -> DisplayUserGroupHierarchySection(viewModel.userGroups)
                UserGroupsViewModel.State.LoadingUserGroupsError -> showErrorDialog(viewModel)
                UserGroupsViewModel.State.Deleting -> { }
                UserGroupsViewModel.State.DeletingError -> showErrorDialog(viewModel)
            }

            if (viewModel.showDropDown) {
                val dropDownMenuContent = remember {
                    createDropdownMenuContent(
                        info = {
                            val userGroupDetailsScreen = UserGroupDetailsScreen(viewModel.selectedUserGroupId, viewModel.selectedUserGroupName) { navigator.pop()}
                            navigator.push(userGroupDetailsScreen)
                        },
                        edit = {
                            val editUserGroupScreen = EditUserGroupScreen(viewModel.selectedUserGroupId) { navigator.pop()}
                            navigator.push(editUserGroupScreen)
                        },
                        delete = {
                            viewModel.deleteUserGroup(viewModel.selectedUserGroupId)
                        },
                    )
                }
                var dropDownHeight by remember { mutableStateOf(0.dp) }
                val density = LocalDensity.current

                DropdownMenu(
                    expanded = viewModel.showDropDown,
                    onDismissRequest = { viewModel.showDropDown = false },
                    offset = viewModel.pressOffset.copy(y = viewModel.pressOffset.y + dropDownHeight),
                    modifier = Modifier.onSizeChanged { dropDownHeight = with(density) { it.height.toDp() } }
                ) {
                    dropDownMenuContent.items.forEach { item ->
                        if (item.icon != null) {
                            DropdownMenuItem(
                                text = { Text(item.name) },
                                onClick = {
                                    viewModel.showDropDown = false
                                    item.onClick()
                                },
                                leadingIcon = { Icon(item.icon, null) }
                            )
                        } else {
                            DropdownMenuItem(
                                text = {
                                    Text(item.name)
                                    viewModel.showDropDown = false
                                },
                                onClick = item.onClick,
                            )
                        }
                    }
                }
            }

            if (viewModel.showErrorDialog) {
                ActionFailedDialog(
                    label = viewModel.errorDialogLabel,
                    body = viewModel.errorDialogBody,
                    onTryAgainRequest = viewModel.onErrorDialogTryAgainRequest,
                    onDismissRequest = viewModel.onErrorDialogDismissRequest,
                    showDismissButton = true
                )
            }
        }
    }



    private fun showErrorDialog(viewModel: UserGroupsViewModel) {
        viewModel.showErrorDialog = true
    }

    private fun createDropdownMenuContent(
        info: () -> Unit,
        edit: () -> Unit,
        delete: () -> Unit,
    ): UserGroupSummaryDropDownMenuContent =
        UserGroupSummaryDropDownMenuContent(
            onInfoClick = info,
            onEditClick = edit,
            onDeleteClick = delete,
        )
}