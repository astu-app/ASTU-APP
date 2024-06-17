package org.astu.feature.single_window.view_models

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.astu.feature.single_window.SingleWindowRepository
import org.astu.feature.single_window.client.models.AddRequestDTO
import org.astu.feature.single_window.client.models.FailRequestDTO
import org.astu.feature.single_window.entities.CreatedRequest
import org.astu.feature.single_window.entities.EmployeeCreatedRequest
import org.astu.feature.single_window.entities.Request
import org.astu.feature.single_window.entities.RequirementField
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.exceptions.ApiException
import org.astu.infrastructure.utils.file.FileUtils
import org.astu.infrastructure.utils.file.PickerMode
import org.astu.infrastructure.utils.file.PickerType
import org.astu.infrastructure.utils.file.extension

class UserCreatedRequestViewModel(var request: CreatedRequest) : ScreenModel, JavaSerializable {
    val error = mutableStateOf<String?>(null)
    private val repository by GlobalDIContext.inject<SingleWindowRepository>()

    fun remove() {
        screenModelScope.launch {
            runCatching {
                repository.removeRequest(request.id)
            }.onFailure {
                println(it)
                println(it.message)
                error.value = "Не удалось обратиться к серверу"
            }.onSuccess {
                println("OK")
            }
        }
    }


}