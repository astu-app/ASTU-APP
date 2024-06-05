package org.astu.feature.bulletinBoard.views.screens.userGroups.actions

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.viewModels.userGroups.actions.EditUserGroupViewModel
import org.astu.feature.bulletinBoard.views.components.userGroups.editing.UserGroupEditor
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

class EditUserGroupScreen(
    private val id: Uuid,
    var onReturn: () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        var onReturnCalled = remember { false }
        val viewModel = rememberScreenModel { EditUserGroupViewModel(id, onReturn)}

        val editor = remember { UserGroupEditor(viewModel) }

        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Редактировать") },
            actions = {
                Button(
                    enabled = editor.canEdit(),
                    onClick = {
                        viewModel.edit()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CurrentColorScheme.surface,
                        disabledContainerColor = CurrentColorScheme.surface,
                        contentColor = CurrentColorScheme.primary,
                    )
                ) {
                    Text(
                        text = "Сохранить",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        ) {
            val state by viewModel.state.collectAsState()
            when (state) {
                EditUserGroupViewModel.State.EditContentLoading -> Loading()
                EditUserGroupViewModel.State.EditingUserGroup -> editor.Content(editor.getDefaultModifier())
                EditUserGroupViewModel.State.EditContentLoadingError -> showErrorDialog(viewModel)
                EditUserGroupViewModel.State.ChangesUploading -> Loading()
                EditUserGroupViewModel.State.ChangesUploadingDone -> if (!onReturnCalled) {
                    onReturnCalled  = true
                    onReturn()
                }
                EditUserGroupViewModel.State.ChangesUploadingError -> showErrorDialog(viewModel)
            }

            if (viewModel.showErrorDialog.value) {
                ActionFailedDialog(
                    label = viewModel.errorDialogLabel.value,
                    body = viewModel.errorDialogBody.value,
                    onTryAgainRequest = viewModel.onErrorDialogTryAgain.value,
                    onDismissRequest = viewModel.onErrorDialogDismiss.value,
                    showDismissButton = true
                )
            }
        }
    }



    private fun showErrorDialog(viewModel: EditUserGroupViewModel) {
        viewModel.showErrorDialog.value = true
    }
}