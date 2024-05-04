package org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent

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
            name = "Завершить опрос",
            icon = Icons.Outlined.StopCircle,
            onClick = onStopSurveyClick
        ),
        AnnouncementDropdownMenuItemContent(
            name = "Удалить",
            icon = Icons.Outlined.Delete,
            onClick = onDeleteClick
        ),
    )
}