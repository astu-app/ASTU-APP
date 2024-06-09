package org.astu.feature.auth.impl

import org.astu.infrastructure.gateway.models.Tokens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.astu.feature.auth.IAccountSecurityManager

/**
 * Реализация хранилища/провайдера информации о средстве авторизации пользователя
 */
class AccountSecurityManager : IAccountSecurityManager {
    private val _data: MutableStateFlow<Tokens?> = MutableStateFlow(null)
    override val data = _data.asStateFlow()

    override fun store(token: Tokens) {
        _data.update { token }
    }

    override fun hasAccess(): Boolean {
        return data.value != null
    }

    override fun logout() {
        _data.update { null }
    }
}

