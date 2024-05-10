package org.astu.feature.bulletinBoard.views.entities.attachments.creation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.astu.feature.bulletinBoard.common.utils.localDateTimeFromComponents
import org.astu.feature.bulletinBoard.views.components.announcements.common.DelayedMomentPicker
import org.astu.feature.bulletinBoard.views.dateTime.getDateString
import org.astu.feature.bulletinBoard.views.dateTime.getTimeString
import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider
import org.astu.infrastructure.components.Paginator
import org.astu.infrastructure.components.SwitchRow
import org.astu.infrastructure.components.common.getButtonColors
import org.astu.infrastructure.theme.CurrentColorScheme
import kotlin.time.Duration

/**
 * Класс для создания опроса
 */
class NewSurvey(private val onSurveyDeleteRequest: () -> Unit) : ContentProvider, DefaultModifierProvider {
    var isAnonymous: MutableState<Boolean> = mutableStateOf(false)
    var resultsOpenBeforeClosing: MutableState<Boolean> = mutableStateOf(true)
    var isAutoClosingEnabled: MutableState<Boolean> = mutableStateOf(false)
    val questions: SnapshotStateList<NewQuestion> = mutableStateListOf()
    var lastQuestionId: Int = 0

    private val autoClosingSwitchTitle = "Автоматическое закрытие"
    val autoClosingEnabled: MutableState<Boolean> = mutableStateOf(false)
    var autoClosingDateMillis: MutableState<Long>
    var autoClosingDateString: MutableState<String>

    var autoClosingTimeHours: MutableState<Int>
    var autoClosingTimeMinutes: MutableState<Int> = mutableStateOf(0)
    var autoClosingTimeString: MutableState<String>



    val autoClosingMoment: LocalDateTime
        get() {
            val dateMillis = autoClosingDateMillis.value
            val hour = autoClosingTimeHours.value
            val minute = autoClosingTimeMinutes.value

            return localDateTimeFromComponents(dateMillis, hour, minute)
        }



    init {
        val question = NewQuestion()
        questions.add(question)

        question.onQuestionDeleteRequest = {
            questions.removeAt(questions.indexOf(question))
            lastQuestionId--
        }

        lastQuestionId++


        val now = Clock.System.now()
        val tomorrow = now + Duration.parse("1d")
        val tomorrowMillis = tomorrow.toEpochMilliseconds()
        autoClosingDateMillis = mutableStateOf(tomorrowMillis)
        autoClosingDateString = mutableStateOf(getDateString(autoClosingDateMillis.value))

        autoClosingTimeHours = mutableStateOf(tomorrow.toLocalDateTime(TimeZone.currentSystemDefault()).hour)
        autoClosingTimeString =
            mutableStateOf(getTimeString(autoClosingTimeHours.value, autoClosingTimeMinutes.value))
    }



    fun isValid(nowMillis: Long): Boolean {
        val surveyAutoClosingMomentMillis = autoClosingDateMillis.value
                .plus(autoClosingDateMillis.value * 3_600_000)
                .plus(autoClosingDateMillis.value * 60_000)

        return nowMillis < surveyAutoClosingMomentMillis
                && questions.all { it.isValid() }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content(modifier: Modifier) {
        val pagerState = rememberPagerState(pageCount = { questions.size })
        val coroutineScope = rememberCoroutineScope()

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    top = 24.dp,
                    bottom = 12.dp,
                    start = 12.dp,
                    end = 12.dp
                )
        ) {
            // Заголовое
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = "Опрос",
                    style = MaterialTheme.typography.titleLarge,
                )
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable { onSurveyDeleteRequest() }
                    )
                }
            }

            // Анонимное голосование
            SwitchRow("Анонимное голосование", isAnonymous)

            // Результаты доступны до закрытия опроса
            SwitchRow("Результаты доступны до закрытия опроса", resultsOpenBeforeClosing)

            // Автоматическое закрытие опроса
            AutoClosingMomentSetter()

            // Текущий вопрос
            HorizontalPager(
                state = pagerState,
                modifier = modifier
            ) { page ->
                val question = questions[page]
                question.Content(question.getDefaultModifier())
            }
            Paginator(pagerState.currentPage, pagerState.pageCount)

            // Добавить вопрос
            ElevatedButton(
                onClick = {
                    val question = NewQuestion()
                    questions.add(question)

                    question.onQuestionDeleteRequest = {
                        questions.removeAt(questions.indexOf(question))
                        lastQuestionId--
                    }

                    lastQuestionId++

                    coroutineScope.launch {
                        if (pagerState.pageCount > 1) {
                            pagerState.animateScrollToPage(pagerState.pageCount)
                        }
                    }
                },
                colors = Color.getButtonColors(
                    containerColor = CurrentColorScheme.secondaryContainer
                ),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Icon(Icons.Outlined.Add, null)
                    Text("Добавить вопрос")
                }
            }
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    }



    @Composable
    private fun AutoClosingMomentSetter() {
        DelayedMomentPicker(
            switchTitle = autoClosingSwitchTitle,
            delayedMomentEnabled = autoClosingEnabled,
            dateMillis = autoClosingDateMillis,
            dateString = autoClosingDateString,
            timeHours = autoClosingTimeHours,
            timeMinutes = autoClosingTimeMinutes,
            timeString = autoClosingTimeString,
        )
    }
}