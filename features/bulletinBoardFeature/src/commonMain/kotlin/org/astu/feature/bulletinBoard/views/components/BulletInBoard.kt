package org.astu.feature.bulletinBoard.views.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.feature.bulletinBoard.views.components.announcements.summary.AnnouncementSummary
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent

@Composable
fun BulletInBoard(
    announcements: List<AnnouncementSummaryContent>
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(announcements.size) { index ->
            AnnouncementSummary(announcements[index])
        }
    }
}