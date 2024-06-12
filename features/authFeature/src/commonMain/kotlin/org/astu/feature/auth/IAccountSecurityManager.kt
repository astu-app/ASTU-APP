package org.astu.feature.auth

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.flow.StateFlow
import org.astu.infrastructure.gateway.models.Tokens

interface IAccountSecurityManager {
    val data: MutableState<Tokens?>

    val currentUserId: String?

    fun store(token: Tokens)

    fun hasAccess(): Boolean

    fun logout()
}