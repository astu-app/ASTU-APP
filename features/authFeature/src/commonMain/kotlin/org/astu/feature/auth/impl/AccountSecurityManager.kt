package org.astu.feature.auth.impl

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.russhwolf.settings.Settings
import com.russhwolf.settings.contains
import kotlinx.coroutines.flow.update
import org.astu.feature.auth.IAccountSecurityManager
import org.astu.feature.auth.jwtDecoding.decodeJwtPayload
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.gateway.models.Tokens

/**
 * Реализация хранилища/провайдера информации о средстве авторизации пользователя
 */
class AccountSecurityManager : IAccountSecurityManager, JavaSerializable {
    private val accessTokenId = "org.astu.app.security.access_token"
    private val refreshTokenId = "org.astu.app.security.refresh_token"

    private val settings: Settings = Settings()

    private val _data: MutableState<Tokens?> = mutableStateOf(initData())
    override val data = _data


    override val currentUserId: String?
        get() {
            val accessToken = _data.value?.accessToken ?: return null
            return decodeJwtPayload(accessToken)?.id
        }


    private fun initData(): Tokens? {
        val accessToken = settings.getString(accessTokenId, "")
        val refreshToken = settings.getString(refreshTokenId, "")

        return if (accessToken.isBlank() || refreshToken.isBlank())
            null
        else
            Tokens(accessToken, refreshToken)
    }


    override fun store(token: Tokens) {
        settings.putString(accessTokenId, token.accessToken)
        settings.putString(refreshTokenId, token.refreshToken)

        _data.value = token
    }

    override fun hasAccess(): Boolean =
        settings.contains(accessTokenId) && settings.contains(refreshTokenId)

    override fun logout() {
        settings.remove(accessTokenId)
        settings.remove(refreshTokenId)

        _data.value = null
    }
}

