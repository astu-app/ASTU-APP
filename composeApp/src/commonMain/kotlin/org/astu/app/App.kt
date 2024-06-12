package org.astu.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.astu.app.notifications.NotificationManager
import org.astu.app.screens.RootScreen
import org.astu.feature.auth.AuthFeatureModule
import org.astu.feature.bulletinBoard.BulletinBoardFeatureModule
import org.astu.feature.chat.ChatFeatureModule
import org.astu.feature.single_window.SingleWindowFeatureModule
import org.astu.feature.universal_request.UniversalRequestFeatureModule
import org.astu.infrastructure.DependencyInjection.GlobalDIContext

@Composable
internal fun App() {
    init()
    AppTheme {
        Navigator(RootScreen())
    }
}

@OptIn(DelicateCoroutinesApi::class)
internal fun init() {
    GlobalDIContext.addModule(AuthFeatureModule.init())

    GlobalDIContext.addModule(MainModule.init())
    GlobalDIContext.addModule(AppModule.init())
    GlobalDIContext.addModule(ChatFeatureModule.init())
    GlobalDIContext.addModule(SingleWindowFeatureModule.init())
    GlobalDIContext.addModule(BulletinBoardFeatureModule.init())
    GlobalDIContext.addModule(UniversalRequestFeatureModule.init())

    GlobalScope.launch { NotificationManager.connect() }
}
