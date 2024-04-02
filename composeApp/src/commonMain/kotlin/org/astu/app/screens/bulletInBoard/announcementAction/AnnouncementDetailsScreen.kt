package org.astu.app.screens.bulletInBoard.announcementAction

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.benasher44.uuid.Uuid
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
        val details = remember { viewModel.content }

        AnnouncementActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Объявление") },
        ) {
            val state by viewModel.state.collectAsState()
            when(state) {
                AnnouncementDetailsViewModel.State.Loading -> Loading()
                AnnouncementDetailsViewModel.State.LoadingDone -> AnnouncementDetails(details)
                AnnouncementDetailsViewModel.State.Error -> TODO()
            }
        }
    }
}