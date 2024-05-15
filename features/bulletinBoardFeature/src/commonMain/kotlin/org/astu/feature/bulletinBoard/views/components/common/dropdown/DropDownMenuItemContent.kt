package org.astu.feature.bulletinBoard.views.components.common.dropdown

import androidx.compose.ui.graphics.vector.ImageVector

class DropDownMenuItemContent(
    val name: String,
    val icon: ImageVector? = null,
    val onClick: () -> Unit,
)