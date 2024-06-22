package org.astu.app.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.infrastructure.AccountUser
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel
import org.astu.infrastructure.exceptions.ApiException
import org.astu.infrastructure.gateway.models.AccountDTO
import org.astu.infrastructure.registration.RegistrationService
import org.astu.infrastructure.security.IAccountSecurityManager
import org.astu.infrastructure.utils.file.FileUtils
import org.astu.infrastructure.utils.file.PickerMode
import org.astu.infrastructure.utils.file.PickerType

class AccountViewModel(private val onLogout: () -> Unit) : StateScreenModel<AccountViewModel.State>(State.Init),
    JavaSerializable {
    sealed class State : JavaSerializable {
        data object Init : State()
        data object Loading : State()
        data class Error(val message: String) : State()
        data object Show : State()
    }

    val account: MutableState<AccountDTO?> = mutableStateOf(null)

    val error: MutableState<String?> = mutableStateOf(null)
    val showErrorDialog: MutableState<Boolean> = mutableStateOf(false)
    val unexpectedErrorTitle: String = "Ошибка"
    val unexpectedErrorBody: String = "Неожиданная ошибка. Повторите попытку"



    init {
        mutableState.value = State.Loading
        val accountUser by GlobalDIContext.inject<AccountUser>()
        screenModelScope.launch {
            runCatching {
                account.value = accountUser.current()
            }.onSuccess {
                mutableState.value = State.Show
            }.onFailure {
                mutableState.value = State.Show
            }
        }
    }

    fun logout() {
        val accountSecurityManager by GlobalDIContext.inject<IAccountSecurityManager>()
        accountSecurityManager.logout()
        onLogout()
    }

    fun uploadFile() {
        val service by GlobalDIContext.inject<RegistrationService>()

        screenModelScope.launch {
            val file = runCatching {
                FileUtils.pickFile(PickerType.File(listOf("csv")), PickerMode.Single)
            }.onFailure {
                error.value = "Не удалось прочитать файл"
            }.getOrNull()

            if (file == null)
                return@launch

            runCatching {
                file.readBytes()

            }.onFailure {
                error.value = "Не удалось прочитать файл"

            }.onSuccess { bytes ->
                runCatching {
                    val errors = service.register(bytes)
                    if (errors.isNotEmpty()) {
                        error.value =
                            "Не удалось зарегистрировать следующие аккаунты: ${errors.sorted().joinToString("\n")}"
                        showErrorDialog.value = true
                    }

                }.onFailure {
                    when (it) {
                        is ApiException -> error.value = it.message
                        else -> error.value = "Не удалось зарегистрировать пользователей"
                    }
                    showErrorDialog.value = true

                }.onSuccess {
                    mutableState.value = State.Show
                }
            }

        }
    }
}