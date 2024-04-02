package org.astu.app.screens.bulletInBoard.announcementAction

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
import org.astu.app.components.Loading
import org.astu.app.components.bulletinBoard.announcements.creation.AnnouncementCreator
import org.astu.app.theme.CurrentColorScheme
import org.astu.app.view_models.bulletInBoard.CreateAnnouncementViewModel

class CreateAnnouncementScreen(private val onReturn: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { CreateAnnouncementViewModel() }
        val creator = AnnouncementCreator(viewModel)

        AnnouncementActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp)
                ) {
                    Text(
                        text = "Новое объявление",
                        modifier = Modifier.weight(1f)
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.weight(0.7f)
                    ) {
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
                }
            }
        ) {
            val state by viewModel.state.collectAsState()
            when(state) {
                CreateAnnouncementViewModel.State.Init -> creator.Content(creator.getDefaultModifier())
                CreateAnnouncementViewModel.State.Uploading -> Loading()
                CreateAnnouncementViewModel.State.UploadingDone -> onReturn()
                CreateAnnouncementViewModel.State.Error -> TODO()
            }
        }
    }
}