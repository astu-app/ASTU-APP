package org.astu.feature.single_window.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.astu.feature.single_window.entities.Template
import org.astu.feature.single_window.view_models.MainRequestViewModel
import org.astu.infrastructure.components.card.Description
import org.astu.infrastructure.components.card.Title


class ListOfServicesSingleWindowScreen(
    val vm: MainRequestViewModel,
    onReturn: (() -> Unit)?,
    onChange: (ServiceScreen) -> Unit
) : ServiceScreen("Список заявлений", onReturn, onChange) {
    @Composable
    override fun Content() {
        val templates = remember { vm.templates }
        val count = templates.value.any()
        if (count)
            VerticalListOfServices()
        else
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Подходящих заявлений нет")
            }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun VerticalListOfServices() {
        val templates = remember { vm.templates }
        val categories = templates.value.groupBy { it.category }

        LazyColumn {
            categories.forEach { category ->
                stickyHeader {
                    Surface(Modifier.fillMaxWidth()) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                modifier = Modifier.padding(top = 10.dp, start = 10.dp),
                                text = category.key,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
//                        Column {
//                            HorizontalDivider(Modifier.fillMaxWidth(), thickness = 2.dp)
//                            Text(
//                                modifier = Modifier.padding(top = 10.dp),
//                                text = category.key,
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 24.sp
//                            )
//                            HorizontalDivider(Modifier.fillMaxWidth(), thickness = 2.dp)
//                        }
                    }
                }
                items(category.value) {
                    ListItem(Modifier.fillMaxWidth(), it)
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                        thickness = 2.dp
                    )
                }
            }
        }
    }

    @Composable
    fun ListItem(modifier: Modifier, template: Template) {
        Card(
            modifier.padding(horizontal = 10.dp, vertical = 10.dp).clickable {
                vm.openRequestScreen(template)
            },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.padding(start = 10.dp)) {
                    Title(template.name)
                    Description(template.description, fontSize = 12.sp)
                }
            }
        }
    }

    val colors: List<Color> = listOf(Color.Gray, Color.Red, Color.Cyan, Color.Green)
}