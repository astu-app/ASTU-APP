package org.astu.feature.bulletinBoard.views.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.infrastructure.components.EmptyContent

@Composable
fun AnnouncementFeed(
    announcements: List<@Composable () -> Unit>,
    state: LazyListState = LazyListState()
) {
    if (announcements.isEmpty()) {
        EmptyContent()
        return
    }

    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize()
    ) {
        items(announcements.size) { index ->
            announcements[index].invoke()
        }
    }
}