package org.astu.feature.bulletinBoard.views.screens.announcements.actions

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
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.viewModels.announcements.actions.AnnouncementDetailsViewModel
import org.astu.feature.bulletinBoard.views.components.announcements.details.AnnouncementDetails
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

class AnnouncementDetailsScreen(
    private val announcementId: Uuid,
    private val onReturn: () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { AnnouncementDetailsViewModel(announcementId) }
        val navigator = LocalNavigator.currentOrThrow

        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Объявление") },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val editAnnouncementScreen = EditAnnouncementScreen(announcementId) { navigator.pop() }
                        navigator.push(editAnnouncementScreen)
                    },
                    containerColor = CurrentColorScheme.tertiaryContainer,
                ) {
                    Icon(Icons.Outlined.Edit, null)
                }
            }
        ) {
            val state by viewModel.state.collectAsState()
            when(state) {
                AnnouncementDetailsViewModel.State.Loading -> { hideErrorDialog(viewModel); Loading() }
                AnnouncementDetailsViewModel.State.LoadingDone -> { hideErrorDialog(viewModel); AnnouncementDetails(viewModel.content.value) }
                AnnouncementDetailsViewModel.State.Error -> showErrorDialog(viewModel)
            }

            if (viewModel.showErrorDialog.value) {
                ActionFailedDialog(
                    label = viewModel.errorDialogLabel.value,
                    body = viewModel.errorDialogBody.value,
                    onTryAgainRequest = {
                        viewModel.loadDetails()
                        viewModel.showErrorDialog.value = false
                    },
                    onDismissRequest = onReturn
                )
            }
        }
    }

    private fun showErrorDialog(viewModel: AnnouncementDetailsViewModel) {
        viewModel.showErrorDialog.value = true
    }

    private fun hideErrorDialog(viewModel: AnnouncementDetailsViewModel) {
        viewModel.showErrorDialog.value = false
    }
}