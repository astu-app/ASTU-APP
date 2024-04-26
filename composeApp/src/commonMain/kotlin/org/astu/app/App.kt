package org.astu.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.astu.app.screens.RootScreen
import org.astu.infrastructure.GlobalDIContext

@Composable
internal fun App() {
    init()
    AppTheme {
        Navigator(RootScreen())
    }
}

internal fun init() {
    val di = AppModule.init()
    GlobalDIContext.addModule(di)
}

expect fun makeHttpClient(): HttpClient

