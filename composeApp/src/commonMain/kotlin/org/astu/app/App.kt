package org.astu.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.astu.app.screens.RootScreen
import org.astu.feature.auth.AuthFeatureModule
import org.astu.feature.chat.ChatFeatureModule
import org.astu.feature.single_window.SingleWindowFeatureModule
import org.astu.infrastructure.DependencyInjection.GlobalDIContext

@Composable
internal fun App() {
    init()
    AppTheme {
        Navigator(RootScreen())
    }
}

internal fun init() {
    GlobalDIContext.addModule(AuthFeatureModule.init())
    GlobalDIContext.addModule(AppModule.init())
    GlobalDIContext.addModule(ChatFeatureModule.init())
    GlobalDIContext.addModule(SingleWindowFeatureModule.init())
}

