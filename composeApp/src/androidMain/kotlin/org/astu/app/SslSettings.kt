package org.astu.app

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