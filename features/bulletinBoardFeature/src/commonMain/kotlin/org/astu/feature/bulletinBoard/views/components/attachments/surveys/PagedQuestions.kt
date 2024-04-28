package org.astu.feature.bulletinBoard.views.components.attachments.surveys

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.questions.Question
import org.astu.feature.bulletinBoard.views.components.attachments.surveys.questions.models.QuestionContentBase
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
        Question(questions[page])
    }
    Paginator(pagerState.currentPage, pagerState.pageCount)
}