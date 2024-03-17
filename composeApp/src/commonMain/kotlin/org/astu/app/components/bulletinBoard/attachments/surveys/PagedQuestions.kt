package org.astu.app.components.bulletinBoard.attachments.surveys

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.app.components.Paginator
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.Question
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.QuestionContentBase

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