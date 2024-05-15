package org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Publish
import org.astu.feature.bulletinBoard.views.components.common.dropdown.DropDownMenuItemContent

class DelayedPublishedAnnouncementDropdownMenuContent(
    onInfoClick: () -> Unit,
    onPublishImmediately: () -> Unit,
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
            name = "Опубликовать сейчас",
            icon = Icons.Outlined.Publish,
            onClick = onPublishImmediately
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