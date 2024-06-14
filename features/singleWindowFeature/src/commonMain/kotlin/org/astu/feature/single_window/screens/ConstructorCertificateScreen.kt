package org.astu.feature.single_window.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.astu.feature.single_window.entities.AddRequirementField
import org.astu.feature.single_window.view_models.ConstructorViewModel
import org.astu.infrastructure.SerializableScreen

class ConstructorCertificateScreen(val onReturn: () -> Unit) : SerializableScreen {
    private lateinit var vm: ConstructorViewModel

    @Composable
    override fun Content() {
        vm = rememberScreenModel { ConstructorViewModel() }
        val done = remember { vm.done }
        if (!done.value)
            list()
        else
            done()
    }

    @Composable
    fun done() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Шаблон был создан.")
            Button(onReturn) {
                Text("Вернуться")
            }
        }
    }

    @Composable
    fun list() {
        val fields = remember { vm.requirements }
        LazyColumn {
            item {
                NameField()
            }
            item {
                DescriptionField()
            }
            item {
                CategoryField()
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(vm.forStudent.value, { vm.forStudent.value = it })
                    Text("Для студентов")
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(vm.forEmployee.value, { vm.forEmployee.value = it })
                    Text("Для сотрудников")
                }
            }
            item {
                fields.value.forEach { requirement ->
                    RequirementItem(requirement)
                }
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                ) {
                    Button(vm::addRequirement) {
                        Text("Добавить поле")
                    }
                    Button(vm::saveRequirement) {
                        Text("Создать заявление")
                    }
                }

            }
        }
    }

    @Composable
    fun NameField() {
        val name = remember { vm.name }
        OutlinedTextField(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            value = name.value,
            onValueChange = { name.value = it },
            maxLines = 1,
            placeholder = { Text("Название справки") }
        )
    }

    @Composable
    fun DescriptionField() {
        val description = remember { vm.description }
        OutlinedTextField(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            value = description.value,
            onValueChange = { description.value = it },
            placeholder = { Text("Описание") }
        )
    }

    @Composable
    fun CategoryField() {
        val category = remember { vm.category }
        OutlinedTextField(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            value = category.value,
            onValueChange = { category.value = it },
            placeholder = { Text("Категория") }
        )
    }

    @Composable
    fun RequirementItem(requirement: AddRequirementField) {
        Column(Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = requirement.name, onValueChange = {
                vm.updateRequirement(requirement.copy(name = it))
            }, placeholder = { Text("Название") })
            Spacer(Modifier.width(4.dp))
            OutlinedTextField(modifier = Modifier.fillMaxWidth(), value = requirement.description, onValueChange = {
                vm.updateRequirement(requirement.copy(description = it))
            }, placeholder = { Text("Описание") })
            Spacer(Modifier.width(4.dp))
            OutlinedButton(
                { vm.minusRequirement(requirement) },
            ) {
                Text("Удалит поле")
            }
            HorizontalDivider()
        }
    }
}