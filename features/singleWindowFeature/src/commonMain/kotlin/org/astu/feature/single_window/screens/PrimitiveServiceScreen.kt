package org.astu.feature.single_window.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.astu.feature.single_window.view_models.MainRequestViewModel
import org.astu.feature.single_window.view_models.RequestViewModel
import org.astu.infrastructure.JavaSerializable

class PrimitiveServiceScreen(
    private val vm: MainRequestViewModel,
    onReturn: (() -> Unit)?,
    onChange: (ServiceScreen) -> Unit
) : ServiceScreen("Заполнение справки", onReturn, onChange), JavaSerializable {
    private lateinit var viewModel: RequestViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { RequestViewModel(vm.currentRequest.value!!) }

        val request = remember { viewModel.request }

        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                    text = request.value.template.name,
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
                    text = request.value.template.description,
                    lineHeight = 26.sp,
                    fontSize = 20.sp
                )
                HorizontalDivider(thickness = 3.dp)
            }
            item {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                    text = "Поля для заполнения",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                HorizontalDivider()
            }
            items(request.value.fields){ prop ->
                Row {
                    Text("${prop.requirement.name} ")
                    Text(prop.requirement.description)
                }

                Row {
                    when (prop.requirement.requirementType.uppercase()) {
                        "STRING" -> {
                            TextField(prop.value, {
                                prop.value = it
                                viewModel.updateField(prop)
                            })
                        }
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.Center) {
                    Button(onClick = {/*EVENT*/ }) {
                        Text("Отправить")
                    }
                }
            }
        }
    }
}