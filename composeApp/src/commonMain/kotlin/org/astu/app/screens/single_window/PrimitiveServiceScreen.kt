package org.astu.app.screens.single_window

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class PrimitiveServiceScreen(private val item: Item, onAddScreen: (ServiceScreen) -> Unit) :
    ServiceScreen(onAddScreen) {

    @Composable
    override fun Content() {
        Scaffold(bottomBar = {
            Box {
                Divider(thickness = 3.dp)
                Button(modifier = Modifier.align(Alignment.BottomCenter), onClick = {/*EVENT*/ }) {
                    Text("Отправить")
                }
            }
        }) { padding ->
            Column(Modifier.padding(padding).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 6.dp),
                    text = item.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                Divider()
                Text(
                    modifier = Modifier
                        .padding(top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp),
                    text = "      ${item.description}",
                    textAlign = TextAlign.Justify,
                    lineHeight = 26.sp,
                    fontSize = 16.sp
                )
                Divider(thickness = 3.dp)
                item.props.forEach { prop ->
                    Row {
                        Text("${prop.name} ")
                        Text(prop.description)
                    }

                    Row {
                        when (prop.type.lowercase()) {
                            "фото" -> {
                                Text("Путь к файлу")
                                Button({}) {
                                    Text("Добавить фото")
                                }
                            }

                            "поле" -> {
                                if (prop.value.value == null)
                                    prop.value.value = ""
                                TextField(prop.value.value!!, { prop.value.value = it })
                            }
                        }
                    }
                    Divider(thickness = 3.dp)
                }
            }
        }
    }
}