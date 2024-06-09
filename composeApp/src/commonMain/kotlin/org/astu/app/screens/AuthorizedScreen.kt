package org.astu.app.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.app.view_models.AuthorizedViewModel
import org.astu.infrastructure.components.NavigationBarScreens

class AuthorizedScreen : Screen {
    private lateinit var viewModel: AuthorizedViewModel

    @Composable
    override fun Content() {
        val nav = LocalNavigator.currentOrThrow
        viewModel = remember { AuthorizedViewModel(nav::popUntilRoot) }
        val screens = remember { viewModel.screens }
        val (selected, setValue) = remember { mutableStateOf(1) }
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