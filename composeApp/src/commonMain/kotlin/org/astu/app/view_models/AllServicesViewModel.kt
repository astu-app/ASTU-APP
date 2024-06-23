package org.astu.app.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.feature.single_window.screens.EmployeeSingleWindowScreen
import org.astu.feature.single_window.screens.MainSingleWindowScreen
import org.astu.feature.single_window.screens.UserSingleWindowScreen
import org.astu.feature.universal_request.screens.TemplateListScreen
import org.astu.infrastructure.AccountUser
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SerializableScreen
import org.astu.infrastructure.StateScreenModel

class AllServicesViewModel : StateScreenModel<AllServicesViewModel.State>(State.Init), JavaSerializable {
    sealed class State : JavaSerializable {
        data object Init : State()
        data object Loading : State()
        data object Show : State()
    }

    val currentScreen: MutableState<SerializableScreen?> = mutableStateOf(null)

    class ServiceInfo(val title: String, val description: String, val onClick: () -> Unit) : JavaSerializable

    val services: MutableState<List<ServiceInfo>> = mutableStateOf(listOf())

    init {
        mutableState.value = State.Loading
        val user by GlobalDIContext.inject<AccountUser>()

        val list = mutableListOf<ServiceInfo>()
        screenModelScope.launch {
            runCatching {
                if (user.checkPerm { it.isEmployee })
                    list.add(ServiceInfo(
                        "Обработка заявлений на справки",
                        "Сервис для обработки заявлений на получение справок"
                    ) {
                        currentScreen.value = EmployeeSingleWindowScreen {
                            currentScreen.value = null
                        }
                    })
            }
            list.add(ServiceInfo(
                "Подача заявлений на справки",
                "Сервис для подачи заявлений на получение справок"
            ) {
                currentScreen.value = MainSingleWindowScreen {
                    currentScreen.value = null
                }
            })
            list.add(ServiceInfo(
                "Список моих заявлений на справку",
                "Просмотр поданных заявлений на получение справок"
            ) {
                currentScreen.value = UserSingleWindowScreen {
                    currentScreen.value = null
                }
            })
            list.add(ServiceInfo(
                "Генерация универсальных заявлений(АСОИУ)",
                "Сервис для генерации заявлений на кафедре АСОИУ"
            ) {
                currentScreen.value = TemplateListScreen {
                    currentScreen.value = null
                }
            })
            services.value = list.toList()
            mutableState.value = State.Show
        }

    }
}