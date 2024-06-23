package org.astu.feature.single_window.view_models

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.feature.single_window.SingleWindowRepository
import org.astu.feature.single_window.client.models.FailRequestDTO
import org.astu.feature.single_window.entities.EmployeeCreatedRequest
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.exceptions.ApiException
import org.astu.infrastructure.utils.file.FileUtils
import org.astu.infrastructure.utils.file.PickerMode
import org.astu.infrastructure.utils.file.PickerType
import org.astu.infrastructure.utils.file.extension

class EmployeeCreatedRequestViewModel(var request: EmployeeCreatedRequest) : ScreenModel, JavaSerializable {
    val comment = mutableStateOf("")
    val error = mutableStateOf<String?>(null)
    private val repository by GlobalDIContext.inject<SingleWindowRepository>()
    val done = mutableStateOf(false)

    fun fail() {
        error.value = null
        if (comment.value.isBlank()) {
            error.value = "Обязательно нужно оставить комментарий"
            return
        }
        val dto = FailRequestDTO(message = comment.value)
        screenModelScope.launch {
            runCatching {
                repository.failRequest(request.id, dto)
            }.onFailure {
                error.value = it.message
            }.onSuccess {
                done.value = true
            }
        }
    }

    fun sendFile() {
        screenModelScope.launch {
            error.value = null

            val file = runCatching {
                FileUtils.pickFile(PickerType.File(listOf("docx", "doc")), PickerMode.Single)
            }.onFailure {
                error.value = "Не удалось прочитать файл"
            }.getOrNull()
            if (file != null) {
                runCatching {
                    file.readBytes()
                }.onFailure {
                    println("Не удалось прочитать файл")
                    error.value = "Не удалось прочитать файл"
                }.onSuccess { bytes ->
                    runCatching {
                        repository.successRequest(request.id, file.name + "." + file.extension, bytes)
                    }.onFailure {
                        when (it) {
                            is ApiException -> error.value = it.message
                            else -> {
                                error.value = "Не удалось загрузить шаблон :("
                            }
                        }
                    }.onSuccess {
                        done.value = true
                        println("OK send file")
                    }
                }
            }
        }
    }

    fun send() {
        error.value = null
        if (comment.value.isBlank()) {
            error.value = "Обязательно нужно оставить комментарий"
            return
        }
        screenModelScope.launch {
            error.value = null
                runCatching {
                    repository.successRequest(request.id, comment.value)
                }.onFailure {
                    when (it) {
                        is ApiException -> error.value = it.message
                        else -> {
                            error.value = "Не удалось загрузить шаблон :("
                        }
                    }
                }.onSuccess {
                    done.value = true
                    println("OK send file")
                }
        }
    }
}