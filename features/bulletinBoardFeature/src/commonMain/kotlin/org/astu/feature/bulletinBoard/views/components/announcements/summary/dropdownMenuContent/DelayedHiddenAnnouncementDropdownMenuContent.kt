package org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import org.astu.feature.bulletinBoard.views.components.common.dropdown.DropDownMenuItemContent

class DelayedHiddenAnnouncementDropdownMenuContent(
    onInfoClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val items: MutableList<DropDownMenuItemContent> = mutableListOf(
        DropDownMenuItemContent(
            name = "Подробности",
            icon = Icons.Outlined.Info,
            onClick = onInfoClick
        ),
        DropDownMenuItemContent(
            name = "Редактировать",
            icon = Icons.Outlined.Edit,
            onClick = onEditClick,
        ),
        DropDownMenuItemContent(
            name = "Удалить",
            icon = Icons.Outlined.Delete,
            onClick = onDeleteClick
        ),
    )
}