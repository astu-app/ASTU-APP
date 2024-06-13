package org.astu.feature.universal_request.view_models

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.extension
import kotlinx.coroutines.launch
import org.astu.feature.universal_request.UniversalTemplateRepository
import org.astu.feature.universal_request.client.models.TemplateInfo
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel
import org.astu.infrastructure.exceptions.ApiException

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
        if(name.value.isBlank() || description.value.isBlank()) {
            error.value = "Все поля должны быть заполнены"
            return
        }

        screenModelScope.launch {
            val file = runCatching {
                 FileKit.pickFile(PickerType.File(listOf("docx", "doc")), PickerMode.Single)
            }.onFailure {
                error.value = "Не удалось прочитать файл"
            }.getOrNull()
            if(file != null) {
                val info = TemplateInfo(name.value, description.value)
                runCatching {
                    file.readBytes()
                }.onFailure {
                    error.value = "Не удалось прочитать файл"
                }.onSuccess {bytes ->
                    runCatching {
                        repository.createTemplate(info, file.name + "." + file.extension, bytes)
                    }.onFailure {
                        when(it){
                            is ApiException -> error.value = it.message
                            else -> {
                                error.value = "Не удалось загрузить шаблон :("
                            }
                        }
                    }.onSuccess {
                        mutableState.value = State.Done
                    }
                }
            }
        }

    }
}