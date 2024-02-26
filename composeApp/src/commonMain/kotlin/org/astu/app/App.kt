package org.astu.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.astu.app.screens.RootScreen
import org.astu.app.screens.chat.ChannelListScreen
import org.astu.app.screens.single_window.MainSingleWindowScreen
import org.astu.app.theme.AppTheme
import org.kodein.di.DI
import org.kodein.di.instance
import kotlin.random.Random

val Int.bool
        get() = if(this == 0) false else false

@Composable
internal fun App() = AppTheme {
    Navigator(RootScreen())
}

