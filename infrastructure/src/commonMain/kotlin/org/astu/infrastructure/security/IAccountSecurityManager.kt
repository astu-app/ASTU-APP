package org.astu.infrastructure.security

import androidx.compose.runtime.MutableState
import org.astu.infrastructure.gateway.models.Tokens

interface IAccountSecurityManager {
    val data: MutableState<Tokens?>

    val currentUserId: String?

    fun store(token: Tokens)

    fun hasAccess(): Boolean

    fun logout()
}