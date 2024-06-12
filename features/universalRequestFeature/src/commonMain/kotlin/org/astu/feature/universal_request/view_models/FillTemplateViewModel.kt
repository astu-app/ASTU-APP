package org.astu.feature.universal_request.view_models

import androidx.compose.runtime.mutableStateOf
import org.astu.feature.universal_request.UniversalTemplateRepository
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.client.models.TemplateFieldDTO
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel

class FillTemplateViewModel(templateDTO: TemplateDTO) : StateScreenModel<FillTemplateViewModel.State>(State.Init), JavaSerializable {
    sealed class State: JavaSerializable {
        data object Init : State()
        data object Loading : State()
        data class Error(val message: String) : State()
        data object Show : State()
    }

    val templateFields = mutableStateOf(listOf<TemplateFieldDTO>())
    val name = templateDTO.name
    val description = templateDTO.description
    val outputPath = mutableStateOf("")

    init {
        val fields = mutableListOf<TemplateFieldDTO>()
        templateDTO.fields.forEach { field ->
            fields.add(TemplateFieldDTO(field, ""))
        }
        templateFields.value = fields
        mutableState.value = State.Show
    }

    fun updateField(field: TemplateFieldDTO) {
        templateFields.value = templateFields.value.map { toReplace ->
            if (toReplace.name == field.name) {
                return@map field
            }
            toReplace
        }
    }

    fun fillTemplate() {
        val repository by GlobalDIContext.inject<UniversalTemplateRepository>()

//        screenModelScope.launch {
//            FileKit.saveFile("swe".toByteArray(), "tmp.doc", "%USERPROFILE%/download")
//        }
    }

    fun setOutputPath(string: String) {
        outputPath.value = string
    }
}