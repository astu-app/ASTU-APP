package org.astu.feature.universal_request.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.feature.universal_request.UniversalTemplateRepository
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.screens.FillTemplateScreen
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.StateScreenModel

class TemplateListViewModel : StateScreenModel<TemplateListViewModel.State>(State.Init), JavaSerializable {
    sealed class State : JavaSerializable{
        data object Init : State()
        data object Loading : State()
        data class Error(val message: String) : State()
        data object ShowList : State()
        data object ShowTemplate : State()
    }

    val templates = mutableStateOf(listOf<TemplateDTO>())
    var screen: MutableState<FillTemplateScreen?> = mutableStateOf(null)
    init {
        val repository by GlobalDIContext.inject<UniversalTemplateRepository>()

        mutableState.value = State.Loading
        screenModelScope.launch {
            val downloadedTemplates = repository.getAllTemplates()
            templates.value = downloadedTemplates
            mutableState.value = State.ShowList
        }
    }

    fun openScreen(template: TemplateDTO) {
        screen.value = FillTemplateScreen(template) {
            screen.value = null
            mutableState.value = State.ShowList
        }
        mutableState.value = State.ShowTemplate
    }
}