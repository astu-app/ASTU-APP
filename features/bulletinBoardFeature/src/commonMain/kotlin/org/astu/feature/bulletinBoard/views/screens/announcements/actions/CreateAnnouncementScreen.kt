package org.astu.feature.bulletinBoard.views.screens.announcements.actions

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
import org.astu.feature.bulletinBoard.viewModels.announcements.actions.CreateAnnouncementViewModel
import org.astu.feature.bulletinBoard.views.components.announcements.creation.AnnouncementCreator
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

class CreateAnnouncementScreen(private val onReturn: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { CreateAnnouncementViewModel(onReturn) }
        val creator = AnnouncementCreator(viewModel)

        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp)
                ) {
                    Text("Новое объявление")
                }
            },
            actions = {
                Button(
                    enabled = creator.canCreate(),
                    onClick = {
                        viewModel.create()
                    },
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
                CreateAnnouncementViewModel.State.CreateContentLoading -> { hideErrorDialog(viewModel); Loading() }
                CreateAnnouncementViewModel.State.CreatingAnnouncement -> { hideErrorDialog(viewModel); creator.Content(creator.getDefaultModifier()) }
                CreateAnnouncementViewModel.State.CreateContentLoadError -> showErrorDialog(viewModel)
                CreateAnnouncementViewModel.State.NewAnnouncementUploading -> { hideErrorDialog(viewModel); Loading() }
                CreateAnnouncementViewModel.State.NewAnnouncementUploadingDone -> { hideErrorDialog(viewModel); onReturn() }
                CreateAnnouncementViewModel.State.NewAnnouncementUploadError -> showErrorDialog(viewModel)
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



    private fun showErrorDialog(viewModel: CreateAnnouncementViewModel) {
        viewModel.showErrorDialog.value = true
    }

    private fun hideErrorDialog(viewModel: CreateAnnouncementViewModel) {
        viewModel.showErrorDialog.value = false
    }
}