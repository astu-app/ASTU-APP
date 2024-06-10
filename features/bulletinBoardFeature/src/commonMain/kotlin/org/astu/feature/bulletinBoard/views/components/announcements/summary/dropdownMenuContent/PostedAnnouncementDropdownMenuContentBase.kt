package org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.StopCircle
import org.astu.feature.bulletinBoard.views.components.common.dropdown.DropDownMenuItemContent

abstract class PostedAnnouncementDropdownMenuContentBase(
    onInfoClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStopSurveyClick: (() -> Unit)?,
) {
    val items: MutableList<DropDownMenuItemContent> = mutableListOf(
        DropDownMenuItemContent(
            name = "Подробности",
            icon = Icons.Outlined.Info,
            onClick = onInfoClick
        ),
        DropDownMenuItemContent(
            name = "Удалить",
            icon = Icons.Outlined.Delete,
            onClick = onDeleteClick
        ),
    )

    init {
        if (onStopSurveyClick != null) {
            items.add(
                1,
                DropDownMenuItemContent(
                    name = "Закрыть опрос",
                    icon = Icons.Outlined.StopCircle,
                    onClick = onStopSurveyClick
                )
            )
        }
    }
}