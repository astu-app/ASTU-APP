package org.astu.feature.bulletinBoard.views.screens.userGroups

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.CreateUserGroupScreen
import org.astu.infrastructure.UnitOfNavigationBar
import org.astu.infrastructure.theme.CurrentColorScheme

// todo контекстное меню дял каждой группы пользователей
class UserGroupsScreen(private val onReturn: () -> Unit) : Screen {
    private val screens: List<UnitOfNavigationBar> =
        listOf(
            UnitOfNavigationBar(
                label = { Text("Иерархия") },
                icon = { Icon(Icons.Default.Newspaper, contentDescription = null) },
                screen = UserGroupHierarchyScreen()
            ),
//            UnitOfNavigationBar(
//                label = { Text("Список") },
//                icon = { Icon(TablerIcons.FileExport, contentDescription = null) },
//                screen = UserGroupListScreen()
//            )
        )

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val (selected, setValue) = remember { mutableStateOf(0) }

        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Группы пользователей") },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val createUserGroupScreen = CreateUserGroupScreen { navigator.pop() }
                        navigator.push(createUserGroupScreen)
                    },
                    containerColor = CurrentColorScheme.tertiaryContainer,
                ) {
                    Icon(Icons.Outlined.Add, "Создать группу пользователей")
                }
            },
//            bottomBar = { NavigationBarScreens(selected, screens, setValue) }
        ) {
            screens.getOrNull(selected)?.screen?.Content()
        }
    }




}