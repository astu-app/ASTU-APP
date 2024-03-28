package org.astu.app.components.bulletinBoard.announcements.summary.dropdownMenuContent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.StopCircle

abstract class DropdownMenuContentBase(
    onInfoClick: () -> Unit,
    onStopSurveyClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val items: MutableList<AnnouncementDropdownMenuItemContent> = mutableListOf(
        AnnouncementDropdownMenuItemContent(
            name = "Подробности",
            icon = Icons.Outlined.Info,
            onClick = onInfoClick
        ),
        AnnouncementDropdownMenuItemContent(
            name = "Остановить опрос объявление",
            icon = Icons.Outlined.StopCircle,
            onClick = onStopSurveyClick
        ),
        AnnouncementDropdownMenuItemContent(
            name = "Удалить объявление",
            icon = Icons.Outlined.Delete,
            onClick = onDeleteClick
        ),
    )
}