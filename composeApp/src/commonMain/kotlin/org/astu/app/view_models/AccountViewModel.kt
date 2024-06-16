package org.astu.app.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.feature.auth.IAccountSecurityManager
import org.astu.feature.universal_request.view_models.AddTemplateViewModel.State
import org.astu.infrastructure.AccountUser
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel
import org.astu.infrastructure.gateway.models.AccountDTO

class AccountViewModel(private val onLogout: () -> Unit) : StateScreenModel<AccountViewModel.State>(State.Init), JavaSerializable {
    sealed class State: JavaSerializable{
        data object Init : State()
        data object Loading : State()
        data class Error(val message: String) : State()
        data object Show : State()
    }

    val account: MutableState<AccountDTO?> = mutableStateOf(null)
    init {
        mutableState.value = State.Loading
        val accountUser by GlobalDIContext.inject<AccountUser>()
        screenModelScope.launch {
            account.value = accountUser.current()
            mutableState.value = State.Show
        }
    }

    fun logout() {
        val accountSecurityManager by GlobalDIContext.inject<IAccountSecurityManager>()
        accountSecurityManager.logout()
        onLogout()
    }
}