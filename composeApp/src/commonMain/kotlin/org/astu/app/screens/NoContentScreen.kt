package org.astu.app.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import compose.icons.TablerIcons
import compose.icons.tablericons.Pacman

class NoContentScreen : Screen {
    @Composable
    override fun Content() {
        Box(Modifier.fillMaxSize(), Alignment.Center) {
            Column {
                Icon(TablerIcons.Pacman, contentDescription = null, Modifier.size(128.dp), tint = Color.Yellow)
                Text("Здесь пока пусто")
            }

        }
    }
}