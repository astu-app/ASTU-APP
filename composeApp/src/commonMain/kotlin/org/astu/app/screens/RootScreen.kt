package org.astu.app.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.app.view_models.RootViewModel

class RootScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { RootViewModel() }
        val nav = LocalNavigator.currentOrThrow
        nav.popAll()
        if (viewModel.hasAccess()) {
            nav.push(AuthorizedScreen())
        } else {
            nav.push(UnauthorizedScreen {
                nav.pop()
            })
        }
    }
}