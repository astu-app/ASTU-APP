package org.astu.app.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.astu.app.view_models.RootViewModel
import org.astu.infrastructure.SerializableScreen

class RootScreen : SerializableScreen {
    private val currentScreen = mutableStateOf<SerializableScreen?>(null)

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { RootViewModel() }
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