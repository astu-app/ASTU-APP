package org.astu.app

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.astu.feature.schedule.ApiTableAstuScheduleDataSource
import org.astu.feature.schedule.ScheduleDataSource
import org.astu.infrastructure.DependencyInjector
import org.astu.infrastructure.FeatureModule
import org.astu.infrastructure.KodeinDependencyInjector
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton

actual object AppModule : FeatureModule {
    actual override fun init(): DependencyInjector = KodeinDependencyInjector(
        DI {
            bind<ScheduleDataSource>() with singleton { ApiTableAstuScheduleDataSource() }
            bind<HttpClient>() with provider {
                HttpClient(OkHttp) {
                    engine {
                        config {
                            sslSocketFactory(SslSettings.sslContext()!!.socketFactory, SslSettings.trustManager())
                            followRedirects(true)
                        }
                    }
                    install(ContentNegotiation) {
                        json()
                    }
                    install(WebSockets) {
                        contentConverter = KotlinxWebsocketSerializationConverter(Json)
                    }
                }
            }
        }
    )
}