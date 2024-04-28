package org.astu.feature.bulletinBoard.views.components.announcements.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.entities.announcement.creation.NewSurvey
import org.astu.infrastructure.components.common.getButtonColors
import org.astu.infrastructure.theme.CurrentColorScheme

/**
 * Секция создания опроса
 * @param newSurvey создаваемый опрос
 */
@Composable
fun AttachSurveySection(newSurvey: MutableState<NewSurvey?>) {
    var surveyAttached by remember { mutableStateOf(false) }
    var showDeleteSurveyDialog by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier.padding(all = 8.dp)
        ) {
            if (!surveyAttached) {
                Button(
                    onClick = {
                        newSurvey.value = NewSurvey { showDeleteSurveyDialog = true }
                        surveyAttached = true
                    },
                    colors = Color.getButtonColors(
                        containerColor = CurrentColorScheme.tertiaryContainer
                    ),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Icon(Icons.Outlined.AddChart, null)
                        Text("Прикрепить опрос")
                    }
                }
            } else {
                newSurvey.value?.Content(newSurvey.value!!.getDefaultModifier())
            }
        }
    }

    if (showDeleteSurveyDialog) {
        AlertDialog(
            title = {
                Text("Удалить опрос?")
            },
            text = {
                Text("Удаление опроса нельзя отменить")
            },
            onDismissRequest = {
                showDeleteSurveyDialog = false
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteSurveyDialog = false
                        surveyAttached = false
                        newSurvey.value = null
                    },
                    colors = Color.getButtonColors(
                        containerColor = CurrentColorScheme.errorContainer,
                        contentColor = CurrentColorScheme.onErrorContainer
                    )
                ) {
                    Text(
                        text = "Удалить",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteSurveyDialog = false },
                    colors = Color.getButtonColors(
                        containerColor = CurrentColorScheme.secondaryContainer,
                        contentColor = CurrentColorScheme.onSecondaryContainer
                    )
                ) {
                    Text(
                        text = "Отменить",
                        modifier = Modifier.clickable { showDeleteSurveyDialog = false },
                        style = MaterialTheme.typography.bodyMedium,
                        color = CurrentColorScheme.onSecondaryContainer
                    )
                }
            },
            containerColor = CurrentColorScheme.secondaryContainer,
            modifier = Modifier
        )
    }
}