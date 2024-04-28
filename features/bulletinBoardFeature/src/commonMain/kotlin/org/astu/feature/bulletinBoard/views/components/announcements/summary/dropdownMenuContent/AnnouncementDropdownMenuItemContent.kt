package org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent

import androidx.compose.ui.graphics.vector.ImageVector

class AnnouncementDropdownMenuItemContent(
    val name: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit,
)