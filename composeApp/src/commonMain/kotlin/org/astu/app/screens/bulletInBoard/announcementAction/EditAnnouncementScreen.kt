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
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.benasher44.uuid.Uuid
import org.astu.app.components.bulletinBoard.announcements.editing.AnnouncementEditor
import org.astu.app.theme.CurrentColorScheme
import org.astu.app.view_models.bulletInBoard.EditAnnouncementViewModel

class EditAnnouncementScreen(
    private val announcementId: Uuid,
    private val onReturn: () -> Unit
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { EditAnnouncementViewModel(announcementId) }
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
                            onClick = { },
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
            editor.Content(editor.getDefaultModifier())
        }
    }
}