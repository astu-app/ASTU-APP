package org.astu.feature.bulletinBoard.views.screens.userGroups

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.feature.bulletinBoard.viewModels.userGroups.UserGroupHierarchyViewModel
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold
import org.astu.feature.bulletinBoard.views.screens.userGroups.actions.CreateUserGroupScreen
import org.astu.infrastructure.theme.CurrentColorScheme

class UserGroupsScreen(private val onReturn: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val viewModel = rememberScreenModel { UserGroupHierarchyViewModel(navigator) }
        val screen = UserGroupHierarchyScreen(viewModel)

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
            actions = {
                // Кнопка обновить
                IconButton(onClick = { viewModel.loadUserGroups() }) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = "Обновить иерархию групп пользователей",
                        tint = CurrentColorScheme.primary
                    )
                }
            }
        ) {
            screen.Content()
        }
    }
}