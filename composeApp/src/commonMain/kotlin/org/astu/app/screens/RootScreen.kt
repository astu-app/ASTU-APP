package org.astu.app.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.app.view_models.RootViewModel
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SerializableScreen

class RootScreen : SerializableScreen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { RootViewModel() }
        val currentScreen = mutableStateOf<SerializableScreen?>(null)
        if (viewModel.hasAccess()) {
            currentScreen.value = AuthorizedScreen()
        } else {
            currentScreen.value = UnauthorizedScreen {
                currentScreen.value = AuthorizedScreen()
            }
        }
        currentScreen.value?.Content()
    }
}