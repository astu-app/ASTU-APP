package org.astu.feature.single_window.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import org.astu.feature.single_window.SingleWindowRepository
import org.astu.feature.single_window.client.models.AddRequirementDTO
import org.astu.feature.single_window.client.models.AddTemplateDTO
import org.astu.feature.single_window.entities.AddRequirementField
import org.astu.infrastructure.AccountUser
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable

class ConstructorViewModel : ScreenModel, JavaSerializable {
    val requirements: MutableState<List<AddRequirementField>> = mutableStateOf(listOf())
    val name = mutableStateOf("")
    val category = mutableStateOf("")
    val forStudent = mutableStateOf(false)
    val forEmployee = mutableStateOf(false)
    val description = mutableStateOf("")

    val done = mutableStateOf(false)

    private val repository by GlobalDIContext.inject<SingleWindowRepository>()
    private val user by GlobalDIContext.inject<AccountUser>()


    fun updateRequirement(addRequirementField: AddRequirementField) {
        requirements.value = requirements.value.map {
            if (it.id == addRequirementField.id)
                return@map addRequirementField
            it
        }
    }

    fun addRequirement() {
        requirements.value = requirements.value.plus(AddRequirementField())
    }

    fun saveRequirement() {
        screenModelScope.launch {
            runCatching {
                val id = user.current()?.departmentId ?: throw RuntimeException()
                val types = repository.getRequirementTypes().single { it.name.uppercase() == "STRING" }
                val dto = AddTemplateDTO(
                    name.value, description.value, category.value, id,
                    requirements.value.map {
                        AddRequirementDTO(types.id, it.name, it.description)
                    },
                    getGroups()
                )
                repository.saveTemplate(dto)
            }.onFailure {
                println(it)
            }.onSuccess {
                done.value = true
            }

        }
    }

    private fun getGroups(): List<AddTemplateDTO.Groups> {
        val groups = mutableListOf<AddTemplateDTO.Groups>()
        if (forStudent.value)
            groups.add(AddTemplateDTO.Groups.Student)

        if (forEmployee.value)
            groups.add(AddTemplateDTO.Groups.Employee)
        return groups.toList()
    }

    fun minusRequirement(addRequirementField: AddRequirementField) {
        requirements.value = requirements.value.filter {
            it.id != addRequirementField.id
        }
    }

}