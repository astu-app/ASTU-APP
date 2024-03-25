package org.astu.feature.schedule.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.astu.feature.schedule.entities.*
import org.astu.feature.schedule.repositories.ScheduleRepository
import org.astu.feature.schedule.entities.Class

class ScheduleViewModel : StateScreenModel<ScheduleViewModel.State>(State.Init) {
    sealed class State {
        data object Init : State()
        data object Loading : State()
        data object PinTable : State()
        data object SearchedTable : State()
        data object Search : State()
        data object FirstLaunch : State()
        data class Error(val message: String) : State()
    }

    private val repository: ScheduleRepository = ScheduleRepository()

    init {
        mutableState.value = State.FirstLaunch
    }

    val pinnedTerm: MutableState<Term?> = mutableStateOf(null)
    val searchedTerm: MutableState<Term?> = mutableStateOf(null)

    val searchValue: MutableState<String> = mutableStateOf("")

    val searchResults: MutableState<List<SearchResult>> = mutableStateOf(listOf())

    val selectedDate = mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val visibleDate = mutableStateOf(selectedDate.value)
    val showWeek = mutableStateOf(true)

    fun find(teacher: Teacher) {
        mutableState.value = State.Loading
        screenModelScope.launch {
            searchedTerm.value = repository.getTerm(teacher)
            mutableState.value = State.SearchedTable
        }
    }

    fun find(group: GroupOfStudents) {
        mutableState.value = State.Loading
        screenModelScope.launch {
            searchedTerm.value = repository.getTerm(group)
            mutableState.value = State.SearchedTable
        }
    }

    fun find(auditory: Auditory) {
        mutableState.value = State.Loading
        screenModelScope.launch {
            searchedTerm.value = repository.getTerm(auditory)
            mutableState.value = State.SearchedTable
        }
    }

    private fun isCurrentWeekIsEven(): Boolean {
        var date = LocalDate(selectedDate.value.year, 9, 1)
        if (selectedDate.value.monthNumber < 9)
            date = date.minus(1, DateTimeUnit.YEAR)
        val weeks = date.daysUntil(selectedDate.value) / 7
        val evenWeek = (weeks % 2)
        return evenWeek.bool
    }

    fun search(value: String) {
        searchValue.value = value
        screenModelScope.launch {
            searchResults.value = repository.find(value)
        }
    }

    fun search(searchResult: SearchResult) {
        mutableState.value = State.Loading
        screenModelScope.launch {
            searchedTerm.value = repository.getTerm(searchResult)
            mutableState.value = State.SearchedTable
        }
    }

    fun isCurrentClass(lesson: Class): Boolean {
        val isDayOfWeekEqual = lesson.dayOfWeek.ordinal == selectedDate.value.dayOfWeek.ordinal
        val isEvenWeekEqual = lesson.isEvenWeek == isCurrentWeekIsEven()
        return isDayOfWeekEqual && isEvenWeekEqual
    }

    fun toSearch() {
        mutableState.value = State.Search
    }

    fun goBackState() = if (pinnedTerm.value != null)
        mutableState.value = State.PinTable
    else
        mutableState.value = State.FirstLaunch

    fun pinTerm() {
        pinnedTerm.value = searchedTerm.value
        searchedTerm.value = null
        mutableState.value = State.PinTable
    }

    fun goPrev() {
        if (showWeek.value)
            visibleDate.value = visibleDate.value.minus(1, DateTimeUnit.WEEK)
        else
            visibleDate.value = visibleDate.value.minus(1, DateTimeUnit.MONTH)
    }

    fun goNext() {
        if (showWeek.value)
            visibleDate.value = visibleDate.value.plus(1, DateTimeUnit.WEEK)
        else
            visibleDate.value = visibleDate.value.plus(1, DateTimeUnit.MONTH)
    }

    fun setupToday() {
        selectedDate.value = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        visibleDate.value = selectedDate.value
    }

    private val Int.bool
        get() = if(this == 0) false else false
}