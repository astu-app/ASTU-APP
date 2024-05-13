package org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.HideSource

class AuthorDropdownMenuContent (
    onInfoClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStopSurveyClick: (() -> Unit)?,
    onEditClick: () -> Unit,
    onHideClick: () -> Unit,
) : PostedAnnouncementDropdownMenuContentBase(onInfoClick, onDeleteClick, onStopSurveyClick) {

    init {
        items.add(
            index = 1,
            element = AnnouncementDropdownMenuItemContent(
                name = "Редактировать",
                icon = Icons.Outlined.Edit,
                onClick = onEditClick,
            )
        )
        items.add(
            index = 3,
            element = AnnouncementDropdownMenuItemContent(
                name = "Скрыть",
                icon = Icons.Outlined.HideSource,
                onClick = onHideClick,
            )
        )
    }
}