package org.astu.feature.single_window.view_models

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.astu.feature.single_window.client.models.AddRequestDTO
import org.astu.feature.single_window.entities.Request
import org.astu.feature.single_window.entities.RequirementField
import org.astu.infrastructure.JavaSerializable

class RequestViewModel(request: Request) : ScreenModel, JavaSerializable {
    val type = MutableStateFlow(request.type)
    val template = MutableStateFlow(request.template)
    val email = MutableStateFlow(request.email)
    val fields = MutableStateFlow(request.fields)
    val request = MutableStateFlow(request)

    fun updateRequest(input: Request) {
        request.update {
            input
        }
    }

    fun updateType(type: AddRequestDTO.Type) {
        this.type.update {
            it
        }
    }

    fun updateType(email: String) {
        this.email.update {
            it
        }
    }

    fun updateField(field: RequirementField<Any>) {
        fields.update { list ->
            list.map {
                if (it.requirement.id == field.requirement.id) field else it
            }
        }
    }
}