package org.astu.app

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
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
    @OptIn(ExperimentalSerializationApi::class)
    actual override fun init(): DependencyInjector = KodeinDependencyInjector (
        DI {
            bind<ScheduleDataSource>() with singleton { ApiTableAstuScheduleDataSource() }
            bind<HttpClient>() with provider {
                HttpClient(Js) {
                    engine {

//                        config {
//                            sslSocketFactory(SslSettings.sslContext()!!.socketFactory, SslSettings.trustManager())
//                            followRedirects(true)
//                            hostnameVerifier(SslSettings.hostNameVerifier())
//                            callTimeout(Duration.ofSeconds(10))
//                        }
                    }
                    defaultRequest {
                      host = "192.168.1.11:7222"
//                        host = "localhost:7222"
//                        host = "10.0.2.2:7222"
                        url {
                            protocol = URLProtocol.HTTPS
                        }
                    }
                    install(ContentNegotiation) {
                        json(Json {
                            prettyPrint = true
                            isLenient = true
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