package org.astu.feature.bulletinBoard.views.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.feature.bulletinBoard.viewModels.announcements.BulletInBoardViewModel
import org.astu.feature.bulletinBoard.views.components.BulletInBoard
import org.astu.feature.bulletinBoard.views.screens.announcementAction.CreateAnnouncementScreen
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

class BulletInBoardScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { BulletInBoardViewModel() }

        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val createAnnouncementScreen = CreateAnnouncementScreen { navigator.pop() }
                        navigator.push(createAnnouncementScreen)
                    },
                    containerColor = CurrentColorScheme.tertiaryContainer,
                ) {
                    Icon(Icons.Outlined.Edit, null)
                }
            },
        ) {
            val state by viewModel.state.collectAsState()
            when(state) {
                BulletInBoardViewModel.State.Loading -> Loading()
                BulletInBoardViewModel.State.LoadingDone -> BulletInBoard(viewModel.content)
                BulletInBoardViewModel.State.Error -> showErrorDialog(viewModel)
            }
        }

        if (viewModel.showErrorDialog.value) {
            ActionFailedDialog(
                label = viewModel.errorDialogLabel.value,
                body = viewModel.errorDialogBody.value,
                onTryAgainRequest = {
                    viewModel.loadAnnouncements()
                    viewModel.showErrorDialog.value = false
                },
                showDismissButton = false
            )
        }
    }



    private fun showErrorDialog(viewModel: BulletInBoardViewModel) {
        viewModel.showErrorDialog.value = true
    }
}