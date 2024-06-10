package org.astu.feature.bulletinBoard.views.components.announcements.summary

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.components.announcements.common.ViewersCount

@Composable
fun AnnouncementFooter(viewed: Int, audienceSize: Int, modifier: Modifier = Modifier.fillMaxWidth()) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
        modifier = modifier
    ) {
        ViewersCount(
            viewed = viewed,
            audienceSize = audienceSize,
            modifier = Modifier
                .requiredWidth(width = 185.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
    }
}