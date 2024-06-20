package org.astu.feature.single_window.view_models

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.benasher44.uuid.uuid4
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.astu.feature.single_window.SingleWindowRepository
import org.astu.feature.single_window.client.models.AddRequestDTO
import org.astu.feature.single_window.entities.Request
import org.astu.feature.single_window.entities.RequirementField
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable

class RequestViewModel(request: Request) : ScreenModel, JavaSerializable {
    val name = request.template.name
    val description = request.template.description
    val template = request.template
    val email = mutableStateOf("")
    val fields = mutableStateOf(request.fields.toMutableList().toList())
    val checked = mutableStateOf(true)
    val state = mutableStateOf(true)
    val done = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)
    private val repository by GlobalDIContext.inject<SingleWindowRepository>()

    fun updateField(field: RequirementField) {
        state.value = false
        fields.value = fields.value.map {
            if (it.requirement.id == field.requirement.id) field else it
        }.toList()
        state.value = true
    }

    fun fill(){
        email.value = "azimusma@gmail.com"
        fields.value.forEach {
            
        }
    }

    fun send() {
        error.value = null
        screenModelScope.launch {
            runCatching {
                val dto = Request(
                    template = template,
                    type = if (checked.value) AddRequestDTO.Type.Email else AddRequestDTO.Type.FaceToFace,
                    email = email.value.ifBlank { null },
                    fields = fields.value
                )
                repository.sendRequest(dto)
            }.onFailure {
                error.value = it.message
            }.onSuccess {
                done.value = true
            }

        }
    }

}