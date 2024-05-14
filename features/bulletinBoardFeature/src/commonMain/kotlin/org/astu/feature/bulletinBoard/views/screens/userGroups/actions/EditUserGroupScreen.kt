package org.astu.feature.bulletinBoard.views.screens.userGroups.actions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold

class EditUserGroupScreen(private val id: Uuid, private val onReturn: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Редактировать группу") },
        ) {
            Text("Редактировать группу пользователей $id")
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
    }
}