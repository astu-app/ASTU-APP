package org.astu.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import io.ktor.client.*
import org.astu.app.dataSources.bulletInBoard.announcements.published.ApiPublishedAnnouncementDataSource
import org.astu.app.dataSources.bulletInBoard.announcements.published.PublishedAnnouncementDataSource
import org.astu.app.screens.RootScreen
import org.astu.app.theme.AppTheme
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton

val Int.bool
        get() = if(this == 0) false else false

@Composable
internal fun App() = AppTheme {
    GlobalDIContext.instance = DI {
        bind<ScheduleDataSource>() with singleton { ApiTableAstuScheduleDataSource() }
        bind<PublishedAnnouncementDataSource>() with singleton { ApiPublishedAnnouncementDataSource() }
        bind<HttpClient>() with provider { makeHttpClient() }
    }

    Navigator(RootScreen())
}

expect fun makeHttpClient(): HttpClient

