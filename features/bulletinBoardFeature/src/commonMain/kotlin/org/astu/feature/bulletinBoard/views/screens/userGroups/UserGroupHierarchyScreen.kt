package org.astu.feature.bulletinBoard.views.screens.userGroups

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.feature.bulletinBoard.viewModels.userGroups.UserGroupHierarchyViewModel
import org.astu.feature.bulletinBoard.views.components.announcements.common.DisplayUserGroupHierarchySection
import org.astu.feature.bulletinBoard.views.components.announcements.creation.DropDownSelector
import org.astu.feature.bulletinBoard.views.components.userGroups.dropdownMenuContent.UserGroupSummaryDropDownMenuContent
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.EditUserGroupScreen
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.UserGroupDetailsScreen
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

class UserGroupHierarchyScreen(private val viewModel: UserGroupHierarchyViewModel) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        viewModel.currentDensity = LocalDensity.current.density

        val state by viewModel.state.collectAsState()
        when (state) {
            UserGroupHierarchyViewModel.State.Loading -> { hideErrorDialog(viewModel); Loading() }
            UserGroupHierarchyViewModel.State.LoadingDone -> { hideErrorDialog(viewModel); ShowLoadedContent(viewModel) }
            UserGroupHierarchyViewModel.State.LoadingUserGroupsError -> showErrorDialog(viewModel)
            UserGroupHierarchyViewModel.State.Deleting -> hideErrorDialog(viewModel)
            UserGroupHierarchyViewModel.State.DeletingError -> showErrorDialog(viewModel)
        }

        if (viewModel.showDropDown) {
            val dropDownMenuContent = remember {
                createDropdownMenuContent(
                    info = {
                        val userGroupDetailsScreen = UserGroupDetailsScreen(viewModel.selectedUserGroupId, viewModel.selectedUserGroupName, viewModel.rootUserGroupId) { navigator.pop()}
                        navigator.push(userGroupDetailsScreen)
                    },
                    edit = {
                        val editUserGroupScreen = EditUserGroupScreen(viewModel.selectedUserGroupId, viewModel.rootUserGroupId) { navigator.pop() }
                        navigator.push(editUserGroupScreen)
                    },
                    delete = {
                        viewModel.deleteUserGroup(viewModel.selectedUserGroupId)
                    },
                )
            }
            val density = LocalDensity.current

            DropdownMenu(
                expanded = viewModel.showDropDown,
                onDismissRequest = { viewModel.showDropDown = false },
                offset = viewModel.pressOffset.copy(
                    y = with(density) { viewModel.selectedUserGroupYPosition.value.toDp() }
                ),
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



    @Composable
    private fun ShowLoadedContent(viewModel: UserGroupHierarchyViewModel) {
        val selectedRootIdSnapshot = viewModel.selectedRootId.value
        if (selectedRootIdSnapshot == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Вы не можете управлять ни одной иерархией групп пользователей",
                    textAlign = TextAlign.Center
                )
            }
            return
        }

        Column {
            // Группа пользователей
            Card(
                colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = "Группа пользователей",
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                )
                DropDownSelector(
                    viewModel.hierarchyRootsForUserGroupSelection.values,
                    viewModel.selectedHierarchyRoot.value,
//                    viewModel.hierarchyRootsForUserGroupSelection[viewModel.selectedRootId.value]!!,
                    viewModel.isSelectUserGroupExpanded
                )
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))

            DisplayUserGroupHierarchySection(listOf(viewModel.userGroups[selectedRootIdSnapshot]!!))
        }
    }

    private fun showErrorDialog(viewModel: UserGroupHierarchyViewModel) {
        viewModel.showErrorDialog = true
    }

    private fun hideErrorDialog(viewModel: UserGroupHierarchyViewModel) {
        viewModel.showErrorDialog = false
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