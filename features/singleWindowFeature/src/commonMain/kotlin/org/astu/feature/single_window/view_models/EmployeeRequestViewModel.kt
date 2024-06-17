package org.astu.feature.single_window.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.astu.feature.single_window.SingleWindowRepository
import org.astu.feature.single_window.entities.CreatedRequest
import org.astu.feature.single_window.entities.EmployeeCreatedRequest
import org.astu.feature.single_window.entities.Request
import org.astu.feature.single_window.entities.Template
import org.astu.feature.single_window.screens.ConstructorCertificateScreen
import org.astu.feature.single_window.screens.EmployeeListOfRequestSingleWindowScreen
import org.astu.feature.single_window.screens.EmployeeRequestScreen
import org.astu.feature.single_window.screens.ServiceScreen
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class EmployeeRequestViewModel : StateScreenModel<EmployeeRequestViewModel.State>(State.Init), JavaSerializable {
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

    val requests = mutableStateOf(mutableListOf<EmployeeCreatedRequest>())

    val prevState: MutableState<State> = mutableStateOf(State.Init)

    val currentScreen: MutableState<ServiceScreen?> = mutableStateOf(null)
    val createScreen: MutableState<ConstructorCertificateScreen?> = mutableStateOf(null)

    val currentRequest: MutableState<EmployeeCreatedRequest?> = mutableStateOf(null)

    private val job: Job

    init {
        prevState.value = mutableState.value
        mutableState.value = State.Loading
        job = screenModelScope.launch {
            loadRequests()
            currentScreen.value = EmployeeListOfRequestSingleWindowScreen(vm = this@EmployeeRequestViewModel, null) {
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
                repository.getEmployeeRequests()
            }.onSuccess {
                requests.value = it.toMutableList()
            }.onFailure {
                println(it)
                println(it.message)
            }
        }
    }

    fun openRequestScreen(request: EmployeeCreatedRequest) = screenModelScope.launch {
        currentRequest.value = request
        currentScreen.value = EmployeeRequestScreen(this@EmployeeRequestViewModel, {
            currentScreen.value = EmployeeListOfRequestSingleWindowScreen(this@EmployeeRequestViewModel, null) {
                currentScreen.value = it
            }
            mutableState.value = State.ShowList
        }, {

        })
        mutableState.value = State.ShowScreen
    }

    fun openConstructor() {
        prevState.value = mutableState.value
        createScreen.value = ConstructorCertificateScreen {
            mutableState.value = prevState.value
            loadRequests()
        }
        mutableState.value = State.ShowCreate
    }
}