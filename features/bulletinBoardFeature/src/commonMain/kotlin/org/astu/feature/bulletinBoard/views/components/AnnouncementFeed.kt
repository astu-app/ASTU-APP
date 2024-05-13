package org.astu.feature.bulletinBoard.views.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnnouncementFeed(
    announcements: List<@Composable () -> Unit>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(announcements.size) { index ->
            announcements[index].invoke()
        }
    }
}