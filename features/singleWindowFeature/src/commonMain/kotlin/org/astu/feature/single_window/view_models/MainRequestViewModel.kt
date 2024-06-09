package org.astu.feature.single_window.view_models

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.astu.feature.single_window.SingleWindowRepository
import org.astu.feature.single_window.entities.Template
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MainRequestViewModel : ScreenModel {
    private val repository by GlobalDIContext.inject<SingleWindowRepository>()

    private val _templates = MutableStateFlow(mutableListOf<Template>())
    val templates = _templates.asStateFlow()
    private val job: Job

    init {
        job = screenModelScope.launch {
            loadTemplates()
            delay(10.toDuration(DurationUnit.SECONDS))

//            while (isActive) {
//                updateChat()
//                delay(10.toDuration(DurationUnit.SECONDS))
//            }
        }
    }

    private suspend fun loadTemplates() {
        runCatching {
            repository.getTemplates()
        }.onSuccess {
            _templates.update { it }
        }
    }

    fun openRequestScreen(template: Template) = screenModelScope.launch{
        val request = repository.makeAddRequest(template)
    }
}