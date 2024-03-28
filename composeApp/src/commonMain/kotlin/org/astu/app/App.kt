package org.astu.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.astu.app.screens.RootScreen
import org.astu.app.theme.AppTheme

val Int.bool
        get() = if(this == 0) false else false

@Composable
internal fun App() = AppTheme {
    Navigator(RootScreen())
}

