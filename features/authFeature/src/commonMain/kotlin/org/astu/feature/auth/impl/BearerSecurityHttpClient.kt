package org.astu.feature.auth.impl

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SecurityHttpClient
import org.astu.infrastructure.security.IAccountSecurityManager

class BearerSecurityHttpClient: SecurityHttpClient, JavaSerializable {
    private val httpClient by GlobalDIContext.inject<HttpClient>()
    private val accountSecurityManager by GlobalDIContext.inject<IAccountSecurityManager>()
//    private val jwtApi by GlobalDIContext.inject<JWTApi>()

    override val instance: HttpClient
        get() = httpClient.config {
            if (!accountSecurityManager.hasAccess())
                TODO("Добавить кейс для не авторизованной попытки авторизоваться")
            val tokens = accountSecurityManager.data.value!!
            install(DefaultRequest){
                header(HttpHeaders.Authorization, "Bearer " + tokens.accessToken)
            }
        }
}