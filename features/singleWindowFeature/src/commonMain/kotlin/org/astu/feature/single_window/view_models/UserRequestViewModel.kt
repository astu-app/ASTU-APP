package org.astu.feature.single_window.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.astu.feature.single_window.SingleWindowRepository
import org.astu.feature.single_window.entities.CreatedRequest
import org.astu.feature.single_window.screens.ConstructorCertificateScreen
import org.astu.feature.single_window.screens.ServiceScreen
import org.astu.feature.single_window.screens.UserListOfRequestSingleWindowScreen
import org.astu.feature.single_window.screens.UserRequestScreen
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class UserRequestViewModel : StateScreenModel<UserRequestViewModel.State>(State.Init), JavaSerializable {
    sealed class State : JavaSerializable {
        data object Init : State()
        data object Loading : State()
        data class Error(val message: String) : State()
        data object ShowList : State()
        data object ShowScreen : State()
        data object ShowCreate : State()
        data object Done : State()
    }

    private val repository by GlobalDIContext.inject<SingleWindowRepository>()

    val requests = mutableStateOf(mutableListOf<CreatedRequest>())

    val prevState: MutableState<State> = mutableStateOf(State.Init)

    val currentScreen: MutableState<ServiceScreen?> = mutableStateOf(null)
    val createScreen: MutableState<ConstructorCertificateScreen?> = mutableStateOf(null)

    val currentRequest: MutableState<CreatedRequest?> = mutableStateOf(null)

    private val job: Job

    init {
        prevState.value = mutableState.value
        mutableState.value = State.Loading
        job = screenModelScope.launch {
            loadRequests()
            currentScreen.value = UserListOfRequestSingleWindowScreen(vm = this@UserRequestViewModel, null) {
                currentScreen.value = it
            }
            mutableState.value = State.ShowList
            delay(10.toDuration(DurationUnit.SECONDS))

//            while (isActive) {
//                updateChat()
//                delay(10.toDuration(DurationUnit.SECONDS))
//            }
        }
    }

    fun loadRequests() {
        screenModelScope.launch {
            runCatching {
                repository.getUserRequests()
            }.onSuccess {
                requests.value = it.toMutableList()
            }.onFailure {
                println(it)
                println(it.message)
            }
        }
    }

    fun openRequestScreen(request: CreatedRequest) = screenModelScope.launch {
        currentRequest.value = request
        currentScreen.value = UserRequestScreen(this@UserRequestViewModel, {
            this@UserRequestViewModel.loadRequests()
            currentScreen.value = UserListOfRequestSingleWindowScreen(this@UserRequestViewModel, null) {
                currentScreen.value = it
            }
            mutableState.value = State.ShowList
        }, {

        })
        mutableState.value = State.ShowScreen
    }
}