package org.astu.app.view_models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import cafe.adriel.voyager.core.model.ScreenModel
import compose.icons.TablerIcons
import compose.icons.tablericons.CalendarEvent
import org.astu.app.screens.AuthScreen
import org.astu.feature.schedule.screens.ScheduleScreen
import org.astu.infrastructure.UnitOfNavigationBar

class UnauthorizedViewModel(onAuth: () -> Unit) : ScreenModel {
    val screens: List<UnitOfNavigationBar> =
        listOf(
            UnitOfNavigationBar(
                label = { Text("Авторизация") },
                icon = { Icon(Icons.AutoMirrored.Filled.Login, contentDescription = null) },
                screen = AuthScreen(onAuth)
            ),
            UnitOfNavigationBar(
                label = { Text("Расписание") },
                icon = { Icon(TablerIcons.CalendarEvent, contentDescription = null) },
                screen = ScheduleScreen()
            )
        )
}