package org.astu.app.components.bulletinBoard.announcements.summary.dropdownMenuContent

import androidx.compose.ui.graphics.vector.ImageVector

class AnnouncementDropdownMenuItemContent(
    val name: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit,
)