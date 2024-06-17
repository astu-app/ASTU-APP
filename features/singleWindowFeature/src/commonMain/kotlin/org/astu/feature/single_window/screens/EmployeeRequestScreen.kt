package org.astu.feature.single_window.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.astu.feature.single_window.client.models.RequestDTO
import org.astu.feature.single_window.view_models.EmployeeCreatedRequestViewModel
import org.astu.feature.single_window.view_models.EmployeeRequestViewModel
import org.astu.feature.single_window.view_models.MainRequestViewModel
import org.astu.feature.single_window.view_models.RequestViewModel
import org.astu.infrastructure.JavaSerializable

class EmployeeRequestScreen(
    private val vm: EmployeeRequestViewModel,
    onReturn: (() -> Unit)?,
    onChange: (ServiceScreen) -> Unit
) : ServiceScreen("Просмотр заявления", onReturn, onChange), JavaSerializable {
    private lateinit var viewModel: EmployeeCreatedRequestViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { EmployeeCreatedRequestViewModel(vm.currentRequest.value!!) }
        val done = remember { viewModel.done }
        if(!done.value)
            ShowList()
        else
            Done()

    }

    @Composable
    private fun ShowList(){
        val request = remember { viewModel.request }
        val comment = remember { viewModel.comment }

        Column(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                text = request.name,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
            HorizontalDivider()
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp),
                text = request.description,
                lineHeight = 26.sp,
                fontSize = 20.sp
            )
            HorizontalDivider(thickness = 3.dp)
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                text = "Заполненные требования",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            HorizontalDivider()
            Column {
                request.fields.forEach { prop ->
                    Column(Modifier.padding(horizontal = 15.dp, vertical = 5.dp)) {
                        Text("Название требования: ${prop.name}")
                        Text("Описание требования: ${prop.description}")
                    }
                    Row {
                        Text(
                            "Значение: ${prop.value as String}",
                            modifier = Modifier.fillMaxWidth().padding(start = 15.dp, top = 5.dp)
                        )
                    }

                }
            }
            HorizontalDivider(thickness = 3.dp)
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                text = "Комментарий",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            HorizontalDivider(thickness = 3.dp)
            TextField(modifier = Modifier.fillMaxWidth(), value = comment.value, onValueChange = {
                comment.value = it
            })
            viewModel.error.value?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (request.type == RequestDTO.Type.Email)
                    Button(onClick = viewModel::sendFile) {
                        Text("Отправить")
                    }
                else
                    Button(onClick = {/*EVENT*/ }) {
                        Text("Уведомить")
                    }
                Button(onClick = viewModel::fail) {
                    Text("Отказать")
                }
            }
        }
    }

    @Composable
    private fun Done(){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Заявка обработана.")
            Button(onReturn ?: {}) {
                Text("Вернуться")
            }
        }
    }
}