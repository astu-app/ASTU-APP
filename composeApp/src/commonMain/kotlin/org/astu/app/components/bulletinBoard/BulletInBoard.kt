package org.astu.app.components.bulletinBoard

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.app.components.bulletinBoard.announcements.summary.AnnouncementSummary
import org.astu.app.entities.bulletInBoard.announcement.summary.AnnouncementSummaryContent

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