package org.astu.feature.auth

import org.astu.infrastructure.gateway.models.Tokens
import kotlinx.coroutines.flow.StateFlow

interface IAccountSecurityManager {
    val data: StateFlow<Tokens?>

    fun store(token: Tokens)

    fun hasAccess(): Boolean

    fun logout()
}