package org.astu.app.screens.bulletInBoard.announcementAction

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.benasher44.uuid.Uuid
import org.astu.app.components.ActionFailedDialog
import org.astu.app.components.Loading
import org.astu.app.components.bulletinBoard.announcements.details.AnnouncementDetails
import org.astu.app.view_models.bulletInBoard.AnnouncementDetailsViewModel

class AnnouncementDetailsScreen(
    private val announcementId: Uuid,
    private val onReturn: () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { AnnouncementDetailsViewModel(announcementId) }

        AnnouncementActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Объявление") },
        ) {
            val state by viewModel.state.collectAsState()
            when(state) {
                AnnouncementDetailsViewModel.State.Loading -> Loading()
                AnnouncementDetailsViewModel.State.LoadingDone -> AnnouncementDetails(viewModel.content.value)
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
}