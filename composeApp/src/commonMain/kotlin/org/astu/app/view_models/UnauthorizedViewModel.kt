package org.astu.app.view_models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import cafe.adriel.voyager.core.model.ScreenModel
import compose.icons.TablerIcons
import compose.icons.tablericons.CalendarEvent
import compose.icons.tablericons.File
import org.astu.app.screens.AuthScreen
import org.astu.feature.schedule.screens.ScheduleScreen
import org.astu.feature.universal_request.screens.TemplateListScreen
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.UnitOfNavigationBar

class UnauthorizedViewModel(onAuth: () -> Unit) : ScreenModel, JavaSerializable {
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
            ),
            UnitOfNavigationBar(
                label = { Text("Универсальные заявки") },
                icon = { Icon(TablerIcons.File, contentDescription = null) },
                screen = TemplateListScreen(){}
            )
        )
}