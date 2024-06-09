package org.astu.feature.single_window.screens

import androidx.compose.foundation.layout.*
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
import org.astu.feature.single_window.entities.Request
import org.astu.feature.single_window.view_models.MainRequestViewModel
import org.astu.feature.single_window.view_models.RequestViewModel

class PrimitiveServiceScreen(
    private val inputRequest: Request,
    onAddScreen: (ServiceScreen) -> Unit
) :
    ServiceScreen(onAddScreen) {
    private lateinit var viewModel: RequestViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { RequestViewModel(inputRequest) }

        val request = remember { viewModel.request }

        Scaffold(bottomBar = {
            Box {
                HorizontalDivider(thickness = 3.dp)
                Button(modifier = Modifier.align(Alignment.BottomCenter), onClick = {/*EVENT*/ }) {
                    Text("Отправить")
                }
            }
        }) { padding ->
            Column(
                Modifier.padding(padding).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
                    text = request.value.template.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                HorizontalDivider()
                Text(
                    modifier = Modifier
                        .padding(top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp),
                    text = "      ${request.value.template.description}",
                    textAlign = TextAlign.Justify,
                    lineHeight = 26.sp,
                    fontSize = 16.sp
                )
                HorizontalDivider(thickness = 3.dp)
                request.value.fields.forEach { prop ->
                    Row {
                        Text("${prop.requirement.name} ")
                        Text(prop.requirement.description)
                    }

                    Row {
                        when (prop.requirement.requirementType.uppercase()) {
                            "FILE" -> {
                                Text("Путь к файлу")
                                Button({}) {
                                    Text("Добавить фото")
                                }
                            }

                            "STRING" -> {
                                TextField(prop.value as String, {
                                    prop.value = it
                                    viewModel.updateField(prop)
                                })
                            }
                        }
                    }
                    HorizontalDivider(thickness = 3.dp)
                }
            }
        }
    }
}