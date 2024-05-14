package org.astu.feature.bulletinBoard.views.screens.userGroups.actions

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.astu.feature.bulletinBoard.views.screens.ActionScreenScaffold

class CreateUserGroupScreen(private val onReturn: () -> Unit) : Screen {
    @Composable
    override fun Content() {
        ActionScreenScaffold(
            onReturn = onReturn,
            topBarTitle = { Text("Новая группа пользователей") },
        ) {
            Text("Создать группу пользователей") // todo
        }
    }
}