package org.astu.feature.universal_request.view_models

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.core.FileKit
import kotlinx.coroutines.launch
import org.astu.feature.universal_request.UniversalTemplateRepository
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.client.models.TemplateFieldDTO
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel
import org.astu.infrastructure.exceptions.ApiException

class FillTemplateViewModel(private val templateDTO: TemplateDTO) :
    StateScreenModel<FillTemplateViewModel.State>(State.Init), JavaSerializable {
    sealed class State : JavaSerializable {
        data object Init : State()
        data object Loading : State()
        data class Error(val message: String) : State()
        data object Show : State()
        data object Done : State()
    }

    val error = mutableStateOf<String?>(null)

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
        val isNotFilled = templateFields.value.any{it.value.isBlank()}

        if(isNotFilled) {
            error.value = "Не все поля заполнены"
            return
        }

        screenModelScope.launch {
            runCatching {
                repository.fillTemplate(templateDTO, templateFields.value)
            }.onFailure {
                when(it){
                    is ApiException -> error.value = it.message
                    else -> {
                        error.value = "Что-то случилось при скачивании :("
                    }
                }

            }.onSuccess { bytes ->
                FileKit.saveFile(bytes, templateDTO.name,"doc")
            }
        }
    }

    fun setOutputPath(string: String) {
        outputPath.value = string
    }
}