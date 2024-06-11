package org.astu.app.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.TablerIcons
import compose.icons.tablericons.CalendarEvent
import compose.icons.tablericons.FileExport
import compose.icons.tablericons.Messages
import compose.icons.tablericons.User
import org.astu.app.view_models.AuthorizedViewModel
import org.astu.feature.bulletinBoard.views.screens.BulletInBoardScreen
import org.astu.feature.chat.screens.ChannelListScreen
import org.astu.feature.schedule.screens.ScheduleScreen
import org.astu.feature.single_window.screens.MainSingleWindowScreen
import org.astu.infrastructure.UnitOfNavigationBar
import org.astu.infrastructure.components.NavigationBarScreens

class AuthorizedScreen : Screen {
    private lateinit var viewModel: AuthorizedViewModel

    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        val screens = remember {
            listOf(
                UnitOfNavigationBar(
                    label = { Text("Объявления") },
                    icon = { Icon(Icons.Default.Newspaper, contentDescription = null) },
                    screen = BulletInBoardScreen()
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
                    screen = AccountScreen {
                        nav::popUntilRoot.invoke()
                        viewModel.logout()
                    }
                )
            )
        }

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