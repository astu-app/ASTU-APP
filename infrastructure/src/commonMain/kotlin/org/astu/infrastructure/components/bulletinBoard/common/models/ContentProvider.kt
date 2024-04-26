package org.astu.app.components.bulletinBoard.common.models

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface ContentProvider {
    @Composable
    fun Content(modifier: Modifier)
}