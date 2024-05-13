package org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Publish

class HiddenAnnouncementDropdownMenuContent(
    onInfoClick: () -> Unit,
    onRestore: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val items: MutableList<AnnouncementDropdownMenuItemContent> = mutableListOf(
        AnnouncementDropdownMenuItemContent(
            name = "Подробности",
            icon = Icons.Outlined.Info,
            onClick = onInfoClick
        ),
        AnnouncementDropdownMenuItemContent(
            name = "Опубликовать",
            icon = Icons.Outlined.Publish,
            onClick = onRestore
        ),
        AnnouncementDropdownMenuItemContent(
            name = "Редактировать",
            icon = Icons.Outlined.Edit,
            onClick = onEditClick,
        ),
        AnnouncementDropdownMenuItemContent(
            name = "Удалить",
            icon = Icons.Outlined.Delete,
            onClick = onDeleteClick
        ),
    )
}