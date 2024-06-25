package org.astu.feature.single_window.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.astu.feature.single_window.view_models.MainRequestViewModel
import org.astu.feature.single_window.view_models.RequestViewModel
import org.astu.infrastructure.JavaSerializable

class PrimitiveServiceScreen(
    private val vm: MainRequestViewModel,
    onReturn: (() -> Unit)?,
    onChange: (ServiceScreen) -> Unit
) : ServiceScreen("Заполнение заявления на справку", onReturn, onChange), JavaSerializable {
    private var viewModel: RequestViewModel = RequestViewModel(vm.currentRequest.value!!)

    @Composable
    private fun showList() {
        val fields = remember((vm.currentRequest.value?.template?.id)) { viewModel.fields }
        Column {
            fields.value.forEach { prop ->
                Column(Modifier.padding(horizontal = 15.dp, vertical = 10.dp)) {
                    Text("Название требования: ${prop.requirement.name} ")
                    Text("Описание требования: ${prop.requirement.description}")
                }

                Row {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = prop.value,
                        onValueChange = {
                            prop.value = it
                            println(fields.value)
                            viewModel.updateField(prop)
                            println(fields.value)
                        })
                }
            }
        }
    }

    @Composable
    private fun showAllList() {
        val checked = remember { viewModel.checked }
        val email = remember { viewModel.email }
        val state = remember { viewModel.state }

        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                text = viewModel.name,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
            HorizontalDivider()
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp),
                text = viewModel.description,
                lineHeight = 26.sp,
                fontSize = 20.sp
            )
            HorizontalDivider(thickness = 3.dp)
            Column {
                Text(
                    "Способ получения",
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Получить лично", modifier = Modifier.padding(horizontal = 20.dp))
                    Switch(checked.value, { checked.value = it })
                    Text("Получить по почте", modifier = Modifier.padding(horizontal = 20.dp))
                }
                if (checked.value)
                    TextField(
                        placeholder = { Text("Адрес электронной почты") },
                        modifier = Modifier.fillMaxWidth(),
                        value = email.value,
                        onValueChange = { email.value = it })
            }
            HorizontalDivider(thickness = 3.dp)
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                text = "Поля для заполнения",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            HorizontalDivider()
            if (state.value)
                showList()
            viewModel.error.value?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { viewModel.send() }) {
                    Text("Отправить заявление в отдел")
                }
            }
            Button(
                onClick = { viewModel.fill() },
                colors = ButtonDefaults.buttonColors()
                    .copy(containerColor = Color.Yellow, contentColor = Color.Black)
            ) {
                Text("Заполнить поля")
            }
        }
    }

    @Composable
    override fun Content() {
        val done = remember { viewModel.done }
        if (!done.value)
            showAllList()
        else
            Done()
    }

    @Composable
    private fun Done() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Заявка была создана.")
            Button(onReturn ?: {}) {
                Text("Вернуться")
            }
        }
    }
}
