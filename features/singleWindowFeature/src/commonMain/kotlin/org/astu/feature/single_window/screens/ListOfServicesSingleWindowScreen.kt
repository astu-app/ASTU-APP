package org.astu.feature.single_window.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import org.astu.feature.single_window.entities.Template
import org.astu.feature.single_window.view_models.MainRequestViewModel
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SerializableScreen
import org.astu.infrastructure.components.card.Description
import org.astu.infrastructure.components.card.Title
import org.astu.infrastructure.components.carousel.Carousel
import org.astu.infrastructure.components.searchbar.*


class ListOfServicesSingleWindowScreen(
    val vm: MainRequestViewModel
) : SerializableScreen {
//    private val list: MutableList<Item> = mutableListOf()
//    private val categories: MutableList<Category> = mutableListOf()

//    init {
//        list.add(Item("Статусы заявок", ""))
//        list.add(Item("Смена фото для биометрии", ""))
//        repeat(32) {
//            list.add(Item("Сервис$it", "Описание справки"))
//        }
//
//        categories.add(Category("Категория 1"))
//        categories.add(Category("Категория 2"))
//
//        categories.forEach { category ->
//            category.items.addAll(
//                arrayOf(
//                    Item("Справка об обучении", "Подтверждает факт поступления студента в университет и содержит информацию о его статусе, например, о том, что он является студентом данного университета"),
//                    Item("Справка об успеваемости", "Содержит информацию о текущих и предыдущих оценках, среднем балле, а также о пройденных предметах и зачетах"),
//                    Item("Справка о смене факультета/специальности", "Выдается в случае, когда студент меняет факультет или специальность, и содержит информацию о новом направлении обучения"),
//                    Item("Справка о переводе", "Выдается, если студент переводится в другой университет или на другую форму обучения, и содержит информацию о переводе и причинах такого решения"),
//                    Item("Справка о стажировке", "Подтверждает факт прохождения студентом стажировки в какой-либо организации и содержит информацию о продолжительности стажировки и полученных навыках."),
//                    Item("Справка о прохождении практики", "Выдается после окончания практики и содержит информацию о ее продолжительности, месте проведения и полученных навыках."),
//                    Item("Справка для предоставления военкомату", "Выдается студентам, которым необходимо предоставить документы в военкомат, и содержит информацию о статусе студента и его образовании"),
//                    Item("Справка для получения льгот", "Выдается студентам, которые имеют право на льготы, например, на льготное проездное или льготные обеды в столовой, и содержит информацию, подтверждающую право на такие льготы"),
//                )
//            )
//            repeat(32) {
//                category.items.add(
//                    Item(
//                        "Справка$it", "Описание справки", listOf(
//                            Prop("Фото студенческого", "Нужно", "Фото"),
//                            Prop("Имя Мамы", "Нужно", "Поле")
//                        )
//                    )
//                )
//            }
//        }
//
//    }

    @Composable
    override fun Content() {
//        SearchBar(
//            Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 10.dp),
//            "",
//            {},
//            CircleShape
//        )
//        HorizontalDivider()
//        HorizontalList(Modifier.padding(horizontal = 10.dp, vertical = 5.dp))
//        HorizontalDivider()
        VerticalListOfServices()
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
                        Column {
                            HorizontalDivider(Modifier.fillMaxWidth(), thickness = 2.dp)
                            Text(
                                modifier = Modifier.padding(top = 10.dp),
                                text = category.key,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                            HorizontalDivider(Modifier.fillMaxWidth(), thickness = 2.dp)
                        }
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
//                IconButton({ onAddFromListScreen(PrimitiveServiceScreen(item, onAddScreen)) }) {
//                    Icon(Icons.Default.KeyboardArrowRight, null)
//                }
            }
        }
    }

//    @Composable
//    fun HorizontalList(modifier: Modifier = Modifier) = Box(modifier) {
//        Carousel(Modifier.height(IntrinsicSize.Max)) {
//            list.forEach {
//                Card(
//                    Modifier.width(140.dp).padding(horizontal = 5.dp, vertical = 5.dp),
//                    colors = CardDefaults.cardColors()
//                ) {
//                    Column(Modifier.padding(horizontal = 5.dp, vertical = 5.dp)) {
//                        Icon(Icons.AutoMirrored.Filled.List, null)
//                        Text(
//                            it.name,
//                            fontSize = 12.sp,
//                            overflow = TextOverflow.Ellipsis,
//                            lineHeight = 12.sp,
//                            maxLines = 3
//                        )
//                        Spacer(Modifier.weight(1f))
//                    }
//                }
//            }
//        }
//    }

    val colors: List<Color> = listOf(Color.Gray, Color.Red, Color.Cyan, Color.Green)
}