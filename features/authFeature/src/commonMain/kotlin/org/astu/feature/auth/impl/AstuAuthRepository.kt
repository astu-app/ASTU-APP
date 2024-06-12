package org.astu.feature.auth.impl

import org.astu.feature.auth.AuthRepository
import org.astu.feature.auth.IAccountSecurityManager
import org.astu.feature.auth.client.apis.AuthApi
import org.astu.feature.auth.client.apis.GoogleApi
import org.astu.feature.auth.client.apis.YandexApi
import org.astu.feature.auth.client.models.JWTLoginDTO
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.GatewayServiceConfig
import org.astu.infrastructure.JavaSerializable

class AstuAuthRepository : AuthRepository, JavaSerializable {
    private val accountSecurityManager by GlobalDIContext.inject<IAccountSecurityManager>()
    private val config by GlobalDIContext.inject<GatewayServiceConfig>()

    override suspend fun authJWT(login: String, password: String) {
        val api = AuthApi(config.url)
        val tokens = api.apiAuthJwtLoginPost(JWTLoginDTO(login, password))
        accountSecurityManager.store(tokens)
    }

    override suspend fun authYandex() {
        val api = YandexApi(config.url)
        val tokens = api.yandexLoginGet()
        accountSecurityManager.store(tokens)
    }

    override suspend fun authGoogle() {
        val api = GoogleApi(config.url)
        val tokens = api.googleLoginGet()
        accountSecurityManager.store(tokens)
    }

    override suspend fun registrationYandex() {
        val api = YandexApi(config.url)
        val tokens = accountSecurityManager.data
        if (tokens.value == null)
            TODO("Добавить обработку")
        val newTokens = api.yandexRegistrationGet(tokens.value!!)
        accountSecurityManager.store(newTokens)
    }

    override suspend fun registrationGoogle() {
        val api = GoogleApi(config.url)
        val tokens = accountSecurityManager.data
        if (tokens.value == null)
            TODO("Добавить обработку")
        val newTokens = api.googleRegistrationGet(tokens.value!!)
        accountSecurityManager.store(newTokens)
    }
}