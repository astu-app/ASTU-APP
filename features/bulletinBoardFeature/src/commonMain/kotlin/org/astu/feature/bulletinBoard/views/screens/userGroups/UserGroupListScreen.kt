package org.astu.feature.bulletinBoard.views.screens.userGroups

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

@Deprecated("Не использовать, пока класс не будет восстановлен")
class UserGroupListScreen : Screen {
    @Composable
    override fun Content() {
        TODO("Доделать список групп пользователей")
//        val navigator = LocalNavigator.currentOrThrow
//        val viewModel = rememberScreenModel { UserGroupListViewModel() }
//        viewModel.currentDensity = LocalDensity.current.density
//
//        val state by viewModel.state.collectAsState()
//        when (state) {
//            UserGroupListViewModel.State.Loading -> Loading()
//            UserGroupListViewModel.State.LoadingDone -> DisplayUserGroupListSection(viewModel.userGroups)
//            UserGroupListViewModel.State.LoadingUserGroupsError -> showErrorDialog(viewModel)
//            UserGroupListViewModel.State.Deleting -> { }
//            UserGroupListViewModel.State.DeletingError -> showErrorDialog(viewModel)
//        }
//
//        if (viewModel.showDropDown) {
//            val dropDownMenuContent = remember {
//                createDropdownMenuContent(
//                    info = {
//                        val userGroupDetailsScreen = UserGroupDetailsScreen(viewModel.selectedUserGroupId, viewModel.selectedUserGroupName) { navigator.pop()}
//                        navigator.push(userGroupDetailsScreen)
//                    },
//                    edit = {
//                        val editUserGroupScreen = EditUserGroupScreen(viewModel.selectedUserGroupId) { navigator.pop()}
//                        navigator.push(editUserGroupScreen)
//                    },
//                    delete = {
//                        viewModel.deleteUserGroup(viewModel.selectedUserGroupId)
//                    },
//                )
//            }
//            var dropDownHeight by remember { mutableStateOf(0.dp) }
//            val density = LocalDensity.current
//
//            DropdownMenu(
//                expanded = viewModel.showDropDown,
//                onDismissRequest = { viewModel.showDropDown = false },
//                offset = viewModel.pressOffset.copy(y = viewModel.pressOffset.y + dropDownHeight),
//                modifier = Modifier.onSizeChanged { dropDownHeight = with(density) { it.height.toDp() } }
//            ) {
//                dropDownMenuContent.items.forEach { item ->
//                    if (item.icon != null) {
//                        DropdownMenuItem(
//                            text = { Text(item.name) },
//                            onClick = {
//                                viewModel.showDropDown = false
//                                item.onClick()
//                            },
//                            leadingIcon = { Icon(item.icon, null) }
//                        )
//                    } else {
//                        DropdownMenuItem(
//                            text = {
//                                Text(item.name)
//                                viewModel.showDropDown = false
//                            },
//                            onClick = item.onClick,
//                        )
//                    }
//                }
//            }
//        }
//
//        if (viewModel.showErrorDialog) {
//            ActionFailedDialog(
//                label = viewModel.errorDialogLabel,
//                body = viewModel.errorDialogBody,
//                onTryAgainRequest = viewModel.onErrorDialogTryAgainRequest,
//                onDismissRequest = viewModel.onErrorDialogDismissRequest,
//                showDismissButton = true
//            )
//        }
    }

//    private fun showErrorDialog(viewModel: UserGroupListViewModel) {
//        viewModel.showErrorDialog = true
//    }
//
//    private fun createDropdownMenuContent(
//        info: () -> Unit,
//        edit: () -> Unit,
//        delete: () -> Unit,
//    ): UserGroupSummaryDropDownMenuContent =
//        UserGroupSummaryDropDownMenuContent(
//            onInfoClick = info,
//            onEditClick = edit,
//            onDeleteClick = delete,
//        )
}