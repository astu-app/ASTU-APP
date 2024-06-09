package org.astu.app.view_models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import cafe.adriel.voyager.core.model.ScreenModel
import compose.icons.TablerIcons
import compose.icons.tablericons.CalendarEvent
import compose.icons.tablericons.FileExport
import compose.icons.tablericons.Messages
import compose.icons.tablericons.User
import org.astu.app.screens.AccountScreen
import org.astu.app.screens.NoContentScreen
import org.astu.feature.chat.screens.ChannelListScreen
import org.astu.feature.schedule.screens.ScheduleScreen
import org.astu.feature.single_window.screens.MainSingleWindowScreen
import org.astu.infrastructure.UnitOfNavigationBar

class AuthorizedViewModel(onLogout: () -> Unit) : ScreenModel {

    val screens: List<UnitOfNavigationBar> =
        listOf(
            UnitOfNavigationBar(
                label = { Text("Доска объявлений") },
                icon = { Icon(Icons.Default.Newspaper, contentDescription = null) },
                screen = NoContentScreen()
            ),
            UnitOfNavigationBar(
                label = { Text("АГТУ.Заявка") },
                icon = { Icon(TablerIcons.FileExport, contentDescription = null) },
                screen = MainSingleWindowScreen()
            ),
            UnitOfNavigationBar(
                label = { Text("Чат") },
                icon = { Icon(TablerIcons.Messages, contentDescription = null) },
                screen = ChannelListScreen()
            ),
            UnitOfNavigationBar(
                label = { Text("Расписание") },
                icon = { Icon(TablerIcons.CalendarEvent, contentDescription = null) },
                screen = ScheduleScreen()
            ),
            UnitOfNavigationBar(
                label = { Text("Аккаунт") },
                icon = { Icon(TablerIcons.User, contentDescription = null) },
                screen = AccountScreen(onLogout)
            )
        )
}