package org.astu.app

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.astu.app.security.SslSettings

class AndroidApp : Application() {
    companion object {
        lateinit var INSTANCE: AndroidApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

actual fun makeHttpClient(): HttpClient {
    return HttpClient(OkHttp) {
        engine {
            config {
                sslSocketFactory(SslSettings.sslContext()!!.socketFactory, SslSettings.trustManager())
                followRedirects(true)
                hostnameVerifier(SslSettings.hostNameVerifier())
            }
        }
        defaultRequest {
//            host = "192.168.1.12:7222"
//            host = "192.168.1.11:7222"
            host = "10.0.2.2:7222"
            url {
                protocol = URLProtocol.HTTPS
            }
        }
        install(ContentNegotiation) {
            json()
        }
        install(WebSockets){
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
    }
}