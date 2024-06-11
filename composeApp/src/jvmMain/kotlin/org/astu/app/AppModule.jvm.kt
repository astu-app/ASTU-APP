package org.astu.app

import SslSettings
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.astu.feature.schedule.ApiTableAstuScheduleDataSource
import org.astu.feature.schedule.ScheduleDataSource
import org.astu.infrastructure.DependencyInjection.DependencyInjector
import org.astu.infrastructure.DependencyInjection.FeatureModule
import org.astu.infrastructure.DependencyInjection.KodeinDependencyInjector
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.provider
import org.kodein.di.singleton
import java.util.concurrent.TimeUnit

actual object AppModule : FeatureModule {
    @OptIn(ExperimentalSerializationApi::class)
    actual override fun init(): DependencyInjector = KodeinDependencyInjector(
        DI {
            bind<ScheduleDataSource>() with singleton { ApiTableAstuScheduleDataSource() }
            bind<HttpClient>() with provider {
                HttpClient(OkHttp) {
                    engine {
                        config {
                            hostnameVerifier { _, _ -> true }
                            sslSocketFactory(SslSettings.sslContext()!!.socketFactory, SslSettings.trustManager())
                            followRedirects(true)
                            callTimeout(3, TimeUnit.MINUTES)
                            writeTimeout(3, TimeUnit.MINUTES)
                            readTimeout(3, TimeUnit.MINUTES)
                        }
                    }
                    install(ContentNegotiation) {
                        json(Json {
                            ignoreUnknownKeys = true
                            encodeDefaults = true
                            explicitNulls = true
                        })
                    }
                    install(WebSockets){
                        contentConverter = KotlinxWebsocketSerializationConverter(Json)
                    }
                }
            }
        }
    )
}