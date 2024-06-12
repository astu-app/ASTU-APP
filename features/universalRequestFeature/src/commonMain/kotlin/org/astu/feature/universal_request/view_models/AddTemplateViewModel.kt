package org.astu.feature.universal_request.view_models

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.extension
import io.ktor.utils.io.core.*
import kotlinx.coroutines.launch
import org.astu.feature.universal_request.UniversalTemplateRepository
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.client.models.TemplateFieldDTO
import org.astu.feature.universal_request.client.models.TemplateInfo
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel

class AddTemplateViewModel : StateScreenModel<AddTemplateViewModel.State>(State.Show) {
    sealed class State : JavaSerializable {
        data object Init : State()
        data object Loading : State()
        data class Error(val message: String) : State()
        data object Show : State()
        data object Done : State()
    }

    val error = mutableStateOf<String?>(null)

    val name = mutableStateOf("")
    val description = mutableStateOf("")
    val outputPath = mutableStateOf("")



    fun uploadFile() {
        val repository by GlobalDIContext.inject<UniversalTemplateRepository>()
        if(name.value.isBlank()) {

        }

        if(description.value.isBlank()) {

        }

        screenModelScope.launch {
            val file = FileKit.pickFile(PickerType.File(listOf("docx", "doc")), PickerMode.Single)
            if(file != null) {
                val info = TemplateInfo(name.value, description.value)
                val bytes = file.readBytes()
                repository.createTemplate(info, file.name + "." + file.extension, bytes)
            }
        }

    }
}