package org.astu.app.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import org.astu.app.view_models.UnauthorizedViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.CalendarEvent
import org.astu.feature.schedule.screens.ScheduleScreen
import org.astu.infrastructure.UnitOfNavigationBar
import org.astu.infrastructure.components.NavigationBarScreens

class UnauthorizedScreen(private val onAuth: () -> Unit) : Screen {
    private lateinit var viewModel: UnauthorizedViewModel

    @Composable
    override fun Content() {
        viewModel = remember { UnauthorizedViewModel(onAuth) }
        val screens = remember { viewModel.screens }
        val (selected, setValue) = remember { mutableStateOf(0) }
        Scaffold(
            bottomBar = {
                NavigationBarScreens(selected, screens, setValue)
            }
        ) {
            Box(Modifier.padding(it)) {
                screens.getOrNull(selected)?.screen?.Content()
            }
        }
    }

}