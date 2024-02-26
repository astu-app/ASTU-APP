import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.astu.app.ApiTableAstuScheduleDataSource
import org.astu.app.App
import org.astu.app.GlobalDIContext
import org.astu.app.ScheduleDataSource
import org.kodein.di.*
import java.awt.Dimension
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


object SslSettings {

    @Suppress("CustomX509TrustManager")
    object TrustAllCerts : X509TrustManager {
        @Suppress("TrustAllX509TrustManager")
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        @Suppress("TrustAllX509TrustManager")
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }

    private val TrustManagers: Array<TrustManager> = arrayOf(TrustAllCerts)

    fun sslContext(): SSLContext? {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, TrustManagers, null)
        return sslContext
    }

    fun trustManager(): X509TrustManager {
        return TrustAllCerts
    }
}

fun main() {
    GlobalDIContext.instance = DI {
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
                install(WebSockets){
                    contentConverter = KotlinxWebsocketSerializationConverter(Json)
                }
            }
        }
    }
    application {
        Window(
            title = "ASTU Client",
            state = rememberWindowState(width = 800.dp, height = 600.dp),
            onCloseRequest = ::exitApplication,
        ) {
            window.minimumSize = Dimension(350, 600)
            App()
        }
    }
}