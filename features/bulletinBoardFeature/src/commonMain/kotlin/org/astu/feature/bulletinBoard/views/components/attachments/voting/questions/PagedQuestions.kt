package org.astu.feature.bulletinBoard.views.components.attachments.voting.questions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.QuestionContentBase
import org.astu.infrastructure.components.Paginator

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagedQuestions(
    questions: List<QuestionContentBase>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {
    val pagerState = rememberPagerState(pageCount = { questions.size })
    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val scope = rememberCoroutineScope()

            val showChevrons = questions.size > 1
            val sideButtonsWeight = remember { 0.025f }

            if (showChevrons) {
                IconButton(
                    onClick = {
                        val prevPage = if (page > 1) page - 1 else 0
                        scope.launch { pagerState.animateScrollToPage(prevPage) }
                    },
                    modifier = Modifier.weight(sideButtonsWeight).fillMaxHeight(),
                ) {
                    Icon(Icons.Outlined.ChevronLeft, null)
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(0.8f)
            ) {
                Question(questions[page])
            }

            if (showChevrons) {
                IconButton(
                    onClick = {
                        val nextPage = if (page < pagerState.pageCount - 1) page + 1 else page
                        scope.launch { pagerState.animateScrollToPage(nextPage) }
                    },
                    modifier = Modifier.weight(sideButtonsWeight).fillMaxHeight(),
                ) {
                    Icon(Icons.Outlined.ChevronRight, null)
                }
            }
        }
    }
    Paginator(pagerState.currentPage, pagerState.pageCount)
}