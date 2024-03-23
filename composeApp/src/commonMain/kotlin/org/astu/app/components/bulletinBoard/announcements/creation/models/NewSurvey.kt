package org.astu.app.components.bulletinBoard.announcements.creation.models

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
import org.astu.app.components.Paginator
import org.astu.app.components.SwitchRow
import org.astu.app.components.bulletinBoard.common.models.ContentProvider
import org.astu.app.components.bulletinBoard.common.models.DefaultModifierProvider
import org.astu.app.components.common.getButtonColors
import org.astu.app.theme.CurrentColorScheme

/**
 * Класс для создания опроса
 */
class NewSurvey(private val onSurveyDeleteRequest: () -> Unit) : ContentProvider, DefaultModifierProvider {
    private var isAnonymous: MutableState<Boolean> = mutableStateOf(false)
    private val questions: SnapshotStateList<NewQuestion> = mutableStateListOf()
    private var lastQuestionId: Int = 0



    init {
        val question = NewQuestion()
        questions.add(question)

        question.onQuestionDeleteRequest = {
            questions.removeAt(questions.indexOf(question))
            lastQuestionId--
        }

        lastQuestionId++
    }



    fun isValid(): Boolean {
        return questions.all { it.isValid() }
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

            SwitchRow("Анонимное голосование", isAnonymous)

            HorizontalPager(
                state = pagerState,
                modifier = modifier
            ) { page ->
                val question = questions[page]
                question.Content(question.getDefaultModifier())
            }
            Paginator(pagerState.currentPage, pagerState.pageCount)

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
}