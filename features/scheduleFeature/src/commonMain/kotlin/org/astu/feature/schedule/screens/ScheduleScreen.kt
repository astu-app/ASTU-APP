package org.astu.feature.schedule.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.DoorFront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import compose.icons.TablerIcons
import compose.icons.tablericons.Compass
import compose.icons.tablericons.User
import kotlinx.datetime.*
import org.astu.feature.schedule.entities.Class
import org.astu.feature.schedule.entities.ClassType
import org.astu.feature.schedule.entities.SearchType
import org.astu.feature.schedule.entities.Term
import org.astu.feature.schedule.view_models.ScheduleViewModel
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SerializableScreen
import org.astu.infrastructure.components.searchbar.SearchBar

class ScheduleScreen : SerializableScreen {
    private lateinit var viewModel: ScheduleViewModel
    private fun convertMonthToString(month: Month): String {
        return when (month) {
            Month.JANUARY -> "Январь"
            Month.FEBRUARY -> "Февраль"
            Month.MARCH -> "Март"
            Month.APRIL -> "Апрель"
            Month.MAY -> "Май"
            Month.JUNE -> "Июнь"
            Month.JULY -> "Июль"
            Month.AUGUST -> "Август"
            Month.SEPTEMBER -> "Сентябрь"
            Month.OCTOBER -> "Октябрь"
            Month.NOVEMBER -> "Ноябрь"
            Month.DECEMBER -> "Декабрь"
            else -> "Undef"
        }
    }

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { ScheduleViewModel() }
        val state by remember{ viewModel.state }
        when (state) {
            ScheduleViewModel.State.Init -> TODO()
            ScheduleViewModel.State.Loading -> Loading()
            ScheduleViewModel.State.PinTable -> MainScheduleTableTopBar()
            ScheduleViewModel.State.Search -> SearchScreen()
            ScheduleViewModel.State.FirstLaunch -> SearchScreen()
            ScheduleViewModel.State.SearchedTable -> SearchedScheduleTableTopBar()
        }
    }

    @Composable
    fun Loading() {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun SearchScreen() {
        val searchValue by remember { viewModel.searchValue }
        val searchResults by remember { viewModel.searchResults }
        val error by remember { viewModel.error }

        Column {
            SearchBar(
                Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 10.dp),
                value = searchValue,
                onValueChange = viewModel::search,
                CircleShape
            )
            if (searchResults.isEmpty()) {
                if(error == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Начните свой поиск")
                    }
                }else{
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(error!!)
                    }
                }


            } else {
                LazyColumn {
                    items(searchResults) {
                        Card(
                            Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)
                                .clickable { viewModel.search(it) }) {
                            Row(
                                Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(searchIcon(it.type), contentDescription = null, modifier = Modifier.size(36.dp))
                                Text(it.name, modifier = Modifier.padding(start = 12.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    private fun searchIcon(type: SearchType): ImageVector = when (type) {
        SearchType.Teacher -> Icons.Default.Man
        SearchType.Group -> Icons.Default.People
        SearchType.Auditory -> Icons.Outlined.DoorFront
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScheduleTableTopBar() {
        val term by remember { viewModel.pinnedTerm }

        Scaffold(topBar = {
            TopAppBar(
                title = { Text(term?.who ?: "") },
                actions = {
                    IconButton(onClick = viewModel::toSearch) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                })
        }) {
            ScheduleTable(Modifier.padding(it), term!!)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchedScheduleTableTopBar() {
        val term by remember { viewModel.searchedTerm }

        Scaffold(topBar = {
            TopAppBar(
                title = { Text(term?.who ?: "") },
                actions = {
                    IconButton(onClick = viewModel::toSearch) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = viewModel::pinTerm) {
                        Icon(Icons.Default.PushPin, contentDescription = null)
                    }
                },
                navigationIcon = {
                    IconButton(viewModel::goBackState) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                })
        }) {
            ScheduleTable(Modifier.padding(it), term!!)
        }
    }

    @Composable
    fun ScheduleTable(modifier: Modifier, term: Term) {
        var selectedDate by remember { viewModel.selectedDate }
        val visibleDate by remember { viewModel.visibleDate }
        var showWeek by remember { viewModel.showWeek }
        Column(modifier) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(viewModel::goPrev) {
                    Icon(Icons.AutoMirrored.Filled.ArrowLeft, contentDescription = null)
                }
                IconButton(viewModel::setupToday) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null)
                }
                Text("${convertMonthToString(visibleDate.month)} ${visibleDate.year}")
                TextButton({ showWeek = !showWeek }, border = BorderStroke(1.dp, Color.Black)) {
                    Text(if (showWeek) "Неделя" else "Месяц")
                }
                IconButton(viewModel::goNext) {
                    Icon(Icons.AutoMirrored.Filled.ArrowRight, contentDescription = null)
                }
            }
            DateTable(visibleDate, selectedDate, showWeek) { selectedDate = it }
            HorizontalDivider()
            Box(Modifier.fillMaxSize()) {
                term.classes.filter { viewModel.isCurrentClass(it) }
                    .sortedBy { it.interval.number }.run {
                        if (isEmpty())
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text("Занятия по расписанию отсутствуют")
                            }
                        else
                            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                                forEach {
                                    ScheduleItem(term.type, it)
                                }
                            }
                    }
            }
        }
    }

    @Composable
    fun DateTable(date: LocalDate, selectedDate: LocalDate, showWeek: Boolean, onClick: (LocalDate) -> Unit) {
        Column(Modifier.fillMaxWidth()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
            ) {
                item { Text("ПН", textAlign = TextAlign.Center) }
                item { Text("ВТ", textAlign = TextAlign.Center) }
                item { Text("СР", textAlign = TextAlign.Center) }
                item { Text("ЧТ", textAlign = TextAlign.Center) }
                item { Text("ПТ", textAlign = TextAlign.Center) }
                item { Text("СБ", textAlign = TextAlign.Center) }
                item { Text("ВС", textAlign = TextAlign.Center) }
                if (showWeek) weekRow(date, selectedDate, onClick)
                else {
                    monthTable(date, selectedDate, onClick)
                }
            }
        }
    }

    private fun LazyGridScope.monthTable(date: LocalDate, selectedDate: LocalDate, onClick: (LocalDate) -> Unit) {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val lastDayOfMonth = LocalDate(date.year, date.monthNumber + 1, 1).minus(1, DateTimeUnit.DAY)
        val firstDayOfMonth = LocalDate(date.year, date.monthNumber, 1)
        val startWeek = firstDayOfMonth.minus(firstDayOfMonth.dayOfWeek.ordinal, DateTimeUnit.DAY)
        val diffDays = startWeek.daysUntil(lastDayOfMonth) + 1
        var dayIterator = startWeek
        items(diffDays) {
            val currentDay = dayIterator
            if (currentDay.monthNumber == date.monthNumber) {
                TextButton(
                    modifier = Modifier.clip(CircleShape).widthIn(10.dp, 16.dp),
                    onClick = { onClick(currentDay) },
                    shape = CircleShape,
                    colors = if (currentDay == selectedDate || currentDay == currentDate)
                        ButtonDefaults.buttonColors(
                            containerColor = if (currentDay == date) Color.hsv(190f, 1f, 1f)
                            else Color.hsv(60f, 0f, 0.75f), contentColor = Color.White
                        )
                    else ButtonDefaults.textButtonColors()
                ) {
                    Text("${currentDay.dayOfMonth}")
                }
            }
            dayIterator = dayIterator.plus(1, unit = DateTimeUnit.DAY)
        }
    }

    private fun LazyGridScope.weekRow(date: LocalDate, selectedDate: LocalDate, onClick: (LocalDate) -> Unit) {
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        var startWeek = date.minus(date.dayOfWeek.ordinal, DateTimeUnit.DAY)
        items(7) {
            val currentDayOfWeek = startWeek
            TextButton(
                modifier = Modifier.clip(CircleShape),
                onClick = { onClick(currentDayOfWeek) },
                shape = CircleShape,
                colors = if (currentDayOfWeek == selectedDate || currentDayOfWeek == currentDate)
                    ButtonDefaults.buttonColors(
                        containerColor = if (currentDayOfWeek == date) Color.hsv(190f, 1f, 1f)
                        else Color.hsv(60f, 0f, 0.75f), contentColor = Color.White
                    )
                else ButtonDefaults.textButtonColors()
            ) {
                Text("${currentDayOfWeek.dayOfMonth}")
            }
            startWeek = startWeek.plus(1, unit = DateTimeUnit.DAY)
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun ScheduleItem(searchType: SearchType, lesson: Class) {
        Column(Modifier.padding(top = 8.dp).fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)) {
                SuggestionChip(
                    {},
                    { Text(lesson.interval.number) },
                    shape = RoundedCornerShape(12.dp),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = Color.hsv(190f, 1f, 1f), labelColor = Color.White
                    )
                )
                SuggestionChip(
                    {},
                    { Text("${lesson.interval.start} - ${lesson.interval.end}") },
                    shape = RoundedCornerShape(12.dp),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = Color.hsv(210f, 1f, 1f), labelColor = Color.White
                    )
                )
                SuggestionChip(
                    {},
                    { Text(lesson.classType.toViewString()) },
                    shape = RoundedCornerShape(12.dp),
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = if (lesson.classType != ClassType.Lecture)
                            Color.hsv(150f, 1f, 0.8f)
                        else
                            Color.hsv(190f, 1f, 1f),
                        labelColor = Color.White

                    )
                )
            }
            Card(Modifier.fillMaxWidth().padding(horizontal = 4.dp)) {
                Text(lesson.discipline.name, modifier = Modifier.padding(top = 6.dp, start = 12.dp))
                FlowRow(
                    Modifier.padding(start = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start)
                ) {
                    if (searchType != SearchType.Teacher) {
                        SuggestionChip({ viewModel.find(lesson.teacher) },
                            { Text(lesson.teacher.name) },
                            icon = { Icon(TablerIcons.User, contentDescription = null) })
                    }
                    if (searchType != SearchType.Auditory) {
                        SuggestionChip({ viewModel.find(lesson.auditory) },
                            { Text(lesson.auditory.name) },
                            icon = { Icon(TablerIcons.Compass, contentDescription = null) })
                    }
                    if (searchType != SearchType.Group) {
                        lesson.groupsOfStudents.forEach {
                            SuggestionChip({ viewModel.find(it) },
                                { Text(it.name) },
                                icon = { Icon(Icons.Default.People, contentDescription = null) })
                        }
                    }
                }
            }
        }
    }

    private fun ClassType.toViewString(): String = when (this) {
        ClassType.Lecture -> "лекция"
        ClassType.Practice -> "практика"
        ClassType.Laboratory -> "лабораторная"
    }
}