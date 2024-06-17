package org.astu.app.view_models

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.feature.auth.AuthRepository
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable

class AuthViewModel : ScreenModel, JavaSerializable {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val error = mutableStateOf<String?>(null)

    fun login(onSuccess: () -> Unit) = screenModelScope.launch {
        val authRepository by GlobalDIContext.inject<AuthRepository>()

        if(password.value.isBlank() || email.value.isBlank()) {
            error.value = "Все поля должны быть заполнены"
            return@launch
        }


        runCatching {
            authRepository.authJWT(email.value, password.value)
        }.onSuccess {
            error.value = null
            password.value = ""
            email.value = ""
            onSuccess()
        }.onFailure {
            password.value = ""
            error.value = "Не удалось подключиться к серверу"
        }
    }
}