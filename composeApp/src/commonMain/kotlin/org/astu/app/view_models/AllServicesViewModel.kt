package org.astu.app.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel

class AllServicesViewModel : StateScreenModel<AllServicesViewModel.State>(State.Init), JavaSerializable {
    sealed class State : JavaSerializable {
        data object Init : State()
        data object Loading : State()
        data object Show: State()
    }

    class ServiceInfo(val title: String, val description: String, val onClick: () -> Unit) : JavaSerializable

    val services: MutableState<List<ServiceInfo>> = mutableStateOf(listOf())

    init {
        mutableState.value = State.Show

        val list = mutableListOf<ServiceInfo>()


    }
}