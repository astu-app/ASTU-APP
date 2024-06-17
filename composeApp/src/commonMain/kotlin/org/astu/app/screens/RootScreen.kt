package org.astu.app.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.astu.app.view_models.RootViewModel
import org.astu.feature.universal_request.screens.TemplateListScreen
import org.astu.infrastructure.SerializableScreen

class RootScreen : SerializableScreen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { RootViewModel() }
        if (viewModel.hasAccess()) {
            viewModel.currentScreen.value = AuthorizedScreen()
        } else {
            viewModel.currentScreen.value = UnauthorizedScreen {
                viewModel.currentScreen.value = AuthorizedScreen()
            }
        }
//        TemplateListScreen({}).Content()
        viewModel.currentScreen.value?.Content()
    }
}