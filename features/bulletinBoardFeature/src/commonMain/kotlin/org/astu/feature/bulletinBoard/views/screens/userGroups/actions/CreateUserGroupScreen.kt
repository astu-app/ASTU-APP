package org.astu.feature.bulletinBoard.views.screens.userGroups.actions

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.viewModels.userGroups.actions.CreateUserGroupViewModel
import org.astu.feature.bulletinBoard.views.components.userGroups.creation.UserGroupCreator
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

class CreateUserGroupScreen(private val rootUserGroupId: Uuid, private val onReturn: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { CreateUserGroupViewModel(rootUserGroupId, onReturn) }
        val creator = UserGroupCreator(viewModel)

        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Новая группа") },
            actions = {
                Button(
                    enabled = viewModel.canCreate(),
                    onClick = { viewModel.create() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CurrentColorScheme.surface,
                        disabledContainerColor = CurrentColorScheme.surface,
                        contentColor = CurrentColorScheme.primary,
                    )
                ) {
                    Text(
                        text = "Создать",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        ) {
            val state by viewModel.state.collectAsState()
            when (state) {
                CreateUserGroupViewModel.State.CreateContentLoading -> { hideErrorDialog(viewModel); Loading() }
                CreateUserGroupViewModel.State.CreateContentLoadingDone -> { hideErrorDialog(viewModel); creator.Content(creator.getDefaultModifier()) }
                CreateUserGroupViewModel.State.CreateContentLoadingError -> showErrorDialog(viewModel)
                CreateUserGroupViewModel.State.NewUserGroupUploading -> { hideErrorDialog(viewModel); Loading() }
                CreateUserGroupViewModel.State.NewUserGroupUploadingDone -> onReturn()
                CreateUserGroupViewModel.State.NewUserGroupUploadingError -> showErrorDialog(viewModel)
            }

            if (viewModel.showErrorDialog) {
                ActionFailedDialog(
                    label = viewModel.errorDialogLabel,
                    body = viewModel.errorDialogBody,
                    onTryAgainRequest = viewModel.onErrorDialogTryAgainRequest,
                    onDismissRequest = viewModel.onErrorDialogDismissRequest
                )
            }
        }
    }

    private fun showErrorDialog(viewModel: CreateUserGroupViewModel) {
        viewModel.showErrorDialog = true
    }

    private fun hideErrorDialog(viewModel: CreateUserGroupViewModel) {
        viewModel.showErrorDialog = false
    }
}