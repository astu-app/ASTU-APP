package org.astu.feature.bulletinBoard.views.screens.userGroups

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.feature.bulletinBoard.viewModels.userGroups.UserGroupsViewModel
import org.astu.feature.bulletinBoard.views.components.announcements.common.DisplayUserGroupHierarchySection
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.CreateUserGroupScreen
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

// todo контекстное меню дял каждой группы пользователей
class UserGroupsScreen(private val onReturn: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { UserGroupsViewModel(navigator) }

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
            }) {
            val state by viewModel.state.collectAsState()
            when (state) {
                UserGroupsViewModel.State.Loading -> Loading()
                UserGroupsViewModel.State.LoadingDone -> DisplayUserGroupHierarchySection(viewModel.userGroups)
                UserGroupsViewModel.State.LoadingUserGroupsError -> showErrorDialog(viewModel)
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

//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Группы пользователей") },
//                    navigationIcon = {
//                        IconButton(onClick = onReturn) {
//                            Icon(
//                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                                contentDescription = "Localized description"
//                            )
//                        }
//                    },
//                )
//            },
//            floatingActionButton = {
//                FloatingActionButton(
//                    onClick = {
////                        val createAnnouncementScreen = CreateAnnouncementScreen { navigator.pop() } // todo
////                        navigator.push(createAnnouncementScreen)
//                    },
//                    containerColor = CurrentColorScheme.tertiaryContainer,
//                ) {
//                    Icon(Icons.Outlined.Edit, null)
//                }
//            },
//        ) {
//            Surface(
//                modifier = Modifier.padding(top = it.calculateTopPadding())
//            ) {
//                val state by viewModel.state.collectAsState()
//                when (state) {
//                    UserGroupsViewModel.State.Loading -> Loading()
//                    UserGroupsViewModel.State.LoadingDone -> DisplayUserGroupHierarchySection(viewModel.userGroups)
//                    UserGroupsViewModel.State.LoadingUserGroupsError -> showErrorDialog(viewModel)
//                }
//
//                if (viewModel.showErrorDialog) {
//                    ActionFailedDialog(
//                        label = viewModel.errorDialogLabel,
//                        body = viewModel.errorDialogBody,
//                        onTryAgainRequest = viewModel.onErrorDialogTryAgainRequest,
//                        onDismissRequest = viewModel.onErrorDialogDismissRequest,
//                        showDismissButton = true
//                    )
//                }
//            }
//        }
    }



    private fun showErrorDialog(viewModel: UserGroupsViewModel) {
        viewModel.showErrorDialog = true
    }
}