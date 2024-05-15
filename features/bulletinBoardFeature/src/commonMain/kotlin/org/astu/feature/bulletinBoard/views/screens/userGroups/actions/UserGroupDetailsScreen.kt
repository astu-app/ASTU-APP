package org.astu.feature.bulletinBoard.views.screens.userGroups.actions

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
import com.benasher44.uuid.uuid4
import org.astu.feature.bulletinBoard.viewModels.userGroups.actions.UserGroupDetailsViewModel
import org.astu.feature.bulletinBoard.views.components.userGroups.UserGroupDetails
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

class UserGroupDetailsScreen(
    private val id: Uuid,
    private val name: String,
    private val onReturn: () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel(tag = uuid4().toString()){ UserGroupDetailsViewModel(id, onReturn) }
        val navigator = LocalNavigator.currentOrThrow

        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text(name) },
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
            val state by viewModel.state.collectAsState()
            when (state) {
                UserGroupDetailsViewModel.State.Loading -> Loading()
                UserGroupDetailsViewModel.State.LoadingDone -> UserGroupDetails(viewModel.content)
                UserGroupDetailsViewModel.State.LoadingError -> showErrorDialog(viewModel)
            }

            if (viewModel.showErrorDialog) {
                ActionFailedDialog(
                    label = viewModel.errorDialogLabel,
                    body = viewModel.errorDialogBody,
                    onTryAgainRequest = viewModel.onErrorDialogTryAgainRequest,
                    onDismissRequest = viewModel.onErrorDialogDismissRequest,
                    showDismissButton = true
                )
            }
        }
    }



    private fun showErrorDialog(viewModel: UserGroupDetailsViewModel) {
        viewModel.showErrorDialog = true
    }
}