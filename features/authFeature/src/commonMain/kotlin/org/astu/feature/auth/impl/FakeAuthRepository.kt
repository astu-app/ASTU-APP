package org.astu.feature.auth.impl

import org.astu.infrastructure.gateway.models.Tokens
import org.astu.feature.auth.AuthRepository
import org.astu.feature.auth.IAccountSecurityManager
import org.astu.infrastructure.DependencyInjection.GlobalDIContext

class FakeAuthRepository : AuthRepository {
    private val accountSecurityManager by GlobalDIContext.inject<IAccountSecurityManager>()
    override suspend fun authJWT(login: String, password: String) {
        accountSecurityManager.store(Tokens("", ""))
    }

    override suspend fun authYandex() {
        accountSecurityManager.store(Tokens("", ""))
    }

    override suspend fun authGoogle() {
        accountSecurityManager.store(Tokens("", ""))
    }

    override suspend fun registrationYandex() {
        accountSecurityManager.store(Tokens("", ""))
    }

    override suspend fun registrationGoogle() {
        accountSecurityManager.store(Tokens("", ""))
    }
}