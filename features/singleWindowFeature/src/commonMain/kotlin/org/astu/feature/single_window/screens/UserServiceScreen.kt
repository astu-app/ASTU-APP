package org.astu.feature.single_window.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.astu.feature.single_window.view_models.MainRequestViewModel
import org.astu.feature.single_window.view_models.RequestViewModel

class UserServiceScreen(
    private val vm: MainRequestViewModel,
    onReturn: (() -> Unit)?,
    onChange: (ServiceScreen) -> Unit
) : ServiceScreen("Заполнение справки", onReturn, onChange) {
    private lateinit var viewModel: RequestViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { RequestViewModel(vm.currentRequest.value!!) }

//        val request = remember { viewModel.request }
        val checked = remember { viewModel.checked }

        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                    text = viewModel.name,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
                HorizontalDivider()
            }
            item {
                Text(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp),
                    text = viewModel.description,
                    lineHeight = 26.sp,
                    fontSize = 20.sp
                )
                HorizontalDivider(thickness = 3.dp)
            }
            item {
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
                            value = viewModel.email.value,
                            onValueChange = { viewModel.email.value = it })
                }
                HorizontalDivider(thickness = 3.dp)
            }
        }
    }
}