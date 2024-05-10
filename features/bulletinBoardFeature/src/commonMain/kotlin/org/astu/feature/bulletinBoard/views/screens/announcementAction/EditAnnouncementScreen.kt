package org.astu.feature.bulletinBoard.views.screens.announcementAction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.viewModels.announcements.EditAnnouncementViewModel
import org.astu.feature.bulletinBoard.views.components.announcements.editing.AnnouncementEditor
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

class EditAnnouncementScreen(
    private val announcementId: Uuid,
    private val onReturn: () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { EditAnnouncementViewModel(announcementId, onReturn) }
        val editor = AnnouncementEditor(viewModel)

        AnnouncementActionScreenScaffold (
            onReturn = onReturn,
            topBarTitle = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp)
                ) {
                    Text(
                        text = "Редактировать",
                        modifier = Modifier.weight(1f)
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(0.9f)
                    ) {
                        Button(
                            enabled = editor.canEdit(),
                            onClick = { viewModel.edit() },
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
                }
            }
        ) {
            val state by viewModel.state.collectAsState()
            when(state) {
                EditAnnouncementViewModel.State.EditContentLoading -> Loading()
                EditAnnouncementViewModel.State.EditingAnnouncement -> editor.Content(editor.getDefaultModifier())
                EditAnnouncementViewModel.State.EditContentLoadingError -> showErrorDialog(viewModel)
                EditAnnouncementViewModel.State.ChangesUploading -> Loading()
                EditAnnouncementViewModel.State.ChangesUploadingDone -> onReturn()
                EditAnnouncementViewModel.State.ChangesUploadingError -> showErrorDialog(viewModel)
            }

            if (viewModel.showErrorDialog.value) {
                ActionFailedDialog(
                    label = viewModel.errorDialogLabel.value,
                    body = viewModel.errorDialogBody.value,
                    onTryAgainRequest = viewModel.onErrorDialogTryAgain.value,
                    onDismissRequest = viewModel.onErrorDialogDismiss.value
                )
            }
        }
    }



    private fun showErrorDialog(viewModel: EditAnnouncementViewModel) {
        viewModel.showErrorDialog.value = true
    }
}