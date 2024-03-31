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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.astu.app.components.bulletinBoard.announcements.creation.AnnouncementCreator
import org.astu.app.theme.CurrentColorScheme

class CreateAnnouncementScreen(private val onReturn: () -> Unit) : Screen {
    private val creator: AnnouncementCreator = AnnouncementCreator()

    @Composable
    override fun Content() {
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
                            onClick = { },
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
            creator.Content(creator.getDefaultModifier())
        }
    }
}