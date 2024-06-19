package org.astu.feature.auth.impl

import org.astu.feature.auth.AuthRepository
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.gateway.models.Tokens
import org.astu.infrastructure.security.IAccountSecurityManager

class FakeAuthRepository : AuthRepository, JavaSerializable {
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