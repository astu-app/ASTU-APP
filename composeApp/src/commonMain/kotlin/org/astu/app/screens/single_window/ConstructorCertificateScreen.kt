package org.astu.app.screens.single_window

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.astu.app.components.ComboBox

class ConstructorCertificateScreen : Screen {

    private val requirements = mutableStateOf(listOf(Requirement(), Requirement(), Requirement(), Requirement()))
    private val name = mutableStateOf("")
    private val description = mutableStateOf("")

    @Composable
    override fun Content() {
        Column {
            NameField()
            DescriptionField()

            key(requirements.value) {
                requirements.value.forEach { requirement ->
                    RequirementItem(requirement) { requirements.value = requirements.value.minus(it) }
                }
            }

            Button({ requirements.value = requirements.value.plus(Requirement()) }) {
                Text("+")
            }
        }
    }

    @Composable
    fun NameField() {
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
        OutlinedTextField(
            modifier = Modifier.padding(10.dp).fillMaxWidth(),
            value = description.value,
            onValueChange = { description.value = it },
            placeholder = { Text("Описание") }
        )
    }

    @Composable
    fun RequirementItem(requirement: Requirement, onDelete: (Requirement) -> Unit) {
        val types = mutableListOf("Фото", "Поле")

        var type by remember { requirement.type }
        var name by remember { requirement.name }
        var description by remember { requirement.description }
        var dismiss by remember { mutableStateOf(false) }

        Row(Modifier.padding(10.dp)) {
            OutlinedTextField(modifier = Modifier.weight(1f), value = name, onValueChange = { name = it })
            Spacer(Modifier.width(4.dp))
            OutlinedTextField(modifier = Modifier.weight(2f), value = description, onValueChange = { description = it })
            Spacer(Modifier.width(4.dp))
            ComboBox(Modifier.weight(1f),
                dismiss,
                onAccept = { dismiss = true },
                onDismiss = { dismiss = false },
                text = { Text(type) }) {
                types.forEach {
                    TextButton({ type = it }, Modifier.fillMaxWidth(), shape = RectangleShape) {
                        Text(it)
                    }
                }
            }
            Spacer(Modifier.width(4.dp))
            OutlinedButton({ onDelete(requirement) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                Text("x")
            }

        }
    }

    data class Requirement(
        var name: MutableState<String> = mutableStateOf(""),
        var description: MutableState<String> = mutableStateOf(""),
        var type: MutableState<String> = mutableStateOf("None"),
    )
}