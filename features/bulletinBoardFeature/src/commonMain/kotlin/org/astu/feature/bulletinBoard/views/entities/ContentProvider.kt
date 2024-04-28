package org.astu.feature.bulletinBoard.views.entities

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface ContentProvider {
    @Composable
    fun Content(modifier: Modifier)
}