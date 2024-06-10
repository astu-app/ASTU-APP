package org.astu.app.security

import android.annotation.SuppressLint
import java.security.cert.X509Certificate
import javax.net.ssl.*

object SslSettings {
    object TrustingHostnameVerifier : HostnameVerifier {
        @SuppressLint("BadHostnameVerifier")
        override fun verify(p0: String?, p1: SSLSession?): Boolean {
            return true
        }
    }

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

    fun hostNameVerifier(): HostnameVerifier {
        return TrustingHostnameVerifier
    }
}