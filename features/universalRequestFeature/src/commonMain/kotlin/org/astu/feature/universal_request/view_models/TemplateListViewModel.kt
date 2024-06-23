package org.astu.feature.universal_request.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.feature.universal_request.UniversalTemplateRepository
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.screens.AddTemplateScreen
import org.astu.feature.universal_request.screens.FillTemplateScreen
import org.astu.infrastructure.AccountUser
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SerializableScreen
import org.astu.infrastructure.StateScreenModel
import org.astu.infrastructure.exceptions.ApiException

class TemplateListViewModel : StateScreenModel<TemplateListViewModel.State>(State.Init),
    JavaSerializable {
    sealed class State : JavaSerializable {
        data object Init : State()
        data object Loading : State()
        data class Error(val message: String, val action: () -> Unit) : State()
        data object ShowList : State()
        data object ShowTemplate : State()
        data object ShowAddScreen : State()
    }

    val templates = mutableStateOf(listOf<TemplateDTO>())
    var screen: MutableState<SerializableScreen?> = mutableStateOf(null)
    var canAdd = mutableStateOf(false)
    private val repository by GlobalDIContext.inject<UniversalTemplateRepository>()

    init {
        mutableState.value = State.Loading
        retryLoad()
    }

    fun openScreen(template: TemplateDTO) {
        screen.value = FillTemplateScreen(template) {
            screen.value = null
            mutableState.value = State.ShowList
        }
        mutableState.value = State.ShowTemplate
    }

    fun retryLoad() {
        screenModelScope.launch {
            runCatching {
                val user by GlobalDIContext.inject<AccountUser>()
                canAdd.value = user.checkPerm { it.isEmployee }
            }

            runCatching {
                repository.getAllTemplates()
            }.onSuccess {
                templates.value = it
                mutableState.value = State.ShowList
            }.onFailure {
                when (it) {
                    is ApiException -> {
                        mutableState.value = State.Error("${it.message}", ::retryLoad)
                    }

                    else -> {
                        mutableState.value = State.Error(
                            "Не удалось загрузить список шаблонов. Нажми на кнопку для повторной попытки",
                            ::retryLoad
                        )
                    }
                }
            }
        }
    }

    fun openAddScreen() {
        screen.value = AddTemplateScreen {
            retryLoad()
            screen.value = null
            mutableState.value = State.ShowList
        }
        mutableState.value = State.ShowAddScreen
    }
}