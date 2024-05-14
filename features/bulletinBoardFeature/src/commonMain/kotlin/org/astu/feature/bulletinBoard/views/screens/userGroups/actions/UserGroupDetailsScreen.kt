package org.astu.feature.bulletinBoard.views.screens.userGroups.actions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.infrastructure.theme.CurrentColorScheme

class UserGroupDetailsScreen(private val id: Uuid, private val onReturn: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Группа пользователей") },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val editUserGroupScreen = EditUserGroupScreen(id) { navigator.pop() }
                        navigator.push(editUserGroupScreen)
                    },
                    containerColor = CurrentColorScheme.tertiaryContainer,
                ) {
                    Icon(Icons.Outlined.Edit, null)
                }
            }
        ) {
                Text("Детали группы пользователей $id")
//            val state by viewModel.state.collectAsState()
//            when (state) {
//                UserGroupsViewModel.State.Loading -> Loading()
//                UserGroupsViewModel.State.LoadingDone -> DisplayUserGroupHierarchySection(viewModel.userGroups)
//                UserGroupsViewModel.State.LoadingUserGroupsError -> showErrorDialog(viewModel)
//            }
//
//            if (viewModel.showErrorDialog) {
//                ActionFailedDialog(
//                    label = viewModel.errorDialogLabel,
//                    body = viewModel.errorDialogBody,
//                    onTryAgainRequest = viewModel.onErrorDialogTryAgainRequest,
//                    onDismissRequest = viewModel.onErrorDialogDismissRequest,
//                    showDismissButton = true
//                )
//            }
        }

//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = { Text("Группа пользователей") },
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
//                        val createUserGroupScreen = UserGroupDetailsScreen(id) { navigator.pop() }
//                        navigator.push(createUserGroupScreen)
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
//                Text(id.toString())
////                val state by viewModel.state.collectAsState()
////                when (state) {
////                    UserGroupsViewModel.State.Loading -> Loading()
////                    UserGroupsViewModel.State.LoadingDone -> DisplayUserGroupHierarchySection(viewModel.userGroups)
////                    UserGroupsViewModel.State.LoadingUserGroupsError -> showErrorDialog(viewModel)
////                }
////            }
////
////            if (viewModel.showErrorDialog) {
////                ActionFailedDialog(
////                    label = viewModel.errorDialogLabel,
////                    body = viewModel.errorDialogBody,
////                    onTryAgainRequest = viewModel.onErrorDialogTryAgainRequest,
////                    onDismissRequest = viewModel.onErrorDialogDismissRequest,
////                    showDismissButton = true
////                )
//            }
//        }
    }
}