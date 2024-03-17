package org.astu.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.astu.app.theme.CurrentColorScheme

@Composable
fun Paginator(
    currentPage: Int,
    totalPages: Int,
) {
    if (totalPages < 2)
        return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom = 8.dp),
    ) {
        repeat(totalPages) { iteration ->
            PaginatorItem(iteration, currentPage)
        }
    }
}

@Composable
private fun PaginatorItem(paginatorItem: Int, currentPage: Int) {
    val color =
        if (currentPage == paginatorItem)
            CurrentColorScheme?.outline ?: Color.DarkGray
        else
            CurrentColorScheme?.outlineVariant ?: Color.LightGray
    val size = if (currentPage == paginatorItem) 8.dp else 6.dp

    Box(
        modifier = Modifier
            .padding(2.dp)
            .clip(CircleShape)
            .background(color)
            .size(size)
    )
}