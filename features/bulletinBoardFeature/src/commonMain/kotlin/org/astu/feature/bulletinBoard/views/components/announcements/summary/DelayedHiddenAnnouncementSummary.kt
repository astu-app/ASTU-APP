package org.astu.feature.bulletinBoard.views.components.announcements.summary

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.viewModels.humanization.humanizeDateTime
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent

@Composable
fun DelayedHiddenAnnouncementSummary(
    content: AnnouncementSummaryContent,
    announcementDropDown: @Composable (DpOffset, MutableState<Boolean>) -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(
            vertical = 8.dp,
            horizontal = 16.dp
        )
) {
    val delayedHiddenMomentText = remember { "Будет скрыто ${humanizeDateTime(content.delayedHidingTime)}" }
    AnnouncementSummary(content, delayedHiddenMomentText, announcementDropDown, modifier)
}