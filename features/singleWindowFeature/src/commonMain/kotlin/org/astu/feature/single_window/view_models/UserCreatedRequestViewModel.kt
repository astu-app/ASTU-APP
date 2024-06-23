package org.astu.feature.single_window.view_models

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.feature.single_window.SingleWindowRepository
import org.astu.feature.single_window.entities.CreatedRequest
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable

class UserCreatedRequestViewModel(var request: CreatedRequest) : ScreenModel, JavaSerializable {
    val error = mutableStateOf<String?>(null)
    private val repository by GlobalDIContext.inject<SingleWindowRepository>()

    fun remove(onSuccess: (()->Unit)?) {
        screenModelScope.launch {
            runCatching {
                repository.removeRequest(request.id)
            }.onFailure {
                println(it)
                println(it.message)
                error.value = "Не удалось обратиться к серверу"
            }.onSuccess {
                onSuccess?.invoke()
                println("OK")
            }
        }
    }


}