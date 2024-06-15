package org.astu.feature.single_window.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.astu.feature.single_window.SingleWindowRepository
import org.astu.feature.single_window.entities.Request
import org.astu.feature.single_window.entities.Template
import org.astu.feature.single_window.screens.ConstructorCertificateScreen
import org.astu.feature.single_window.screens.ListOfServicesSingleWindowScreen
import org.astu.feature.single_window.screens.PrimitiveServiceScreen
import org.astu.feature.single_window.screens.ServiceScreen
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MainRequestViewModel : StateScreenModel<MainRequestViewModel.State>(State.Init), JavaSerializable {
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

    val templates = mutableStateOf(mutableListOf<Template>())

    val prevState: MutableState<State> = mutableStateOf(State.Init)

    val currentScreen: MutableState<ServiceScreen?> = mutableStateOf(null)
    val createScreen: MutableState<ConstructorCertificateScreen?> = mutableStateOf(null)

    val currentRequest: MutableState<Request?> = mutableStateOf(null)

    private val job: Job

    init {
        prevState.value = mutableState.value
        mutableState.value = State.Loading
        job = screenModelScope.launch {
            loadTemplates()
            currentScreen.value = ListOfServicesSingleWindowScreen(vm = this@MainRequestViewModel, null) {
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

    fun loadTemplates() {
        screenModelScope.launch {
            runCatching {
                repository.getTemplates()
            }.onSuccess {
                templates.value = it.toMutableList()
            }
        }
    }

    fun openRequestScreen(template: Template) = screenModelScope.launch {
        val request = repository.makeAddRequest(template)
        currentRequest.value = request
        currentScreen.value = PrimitiveServiceScreen(this@MainRequestViewModel, {
            currentScreen.value = ListOfServicesSingleWindowScreen(this@MainRequestViewModel, null) {
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
            loadTemplates()
        }
        mutableState.value = State.ShowCreate
    }
}