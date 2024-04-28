package org.astu.feature.bulletinBoard.views.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.feature.bulletinBoard.views.components.announcements.summary.AnnouncementSummary
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent

@Composable
fun BulletInBoard(
    announcements: List<AnnouncementSummaryContent>,
    scrollState: ScrollState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        announcements.forEach { announcement ->
            AnnouncementSummary(announcement)
        }
    }
}