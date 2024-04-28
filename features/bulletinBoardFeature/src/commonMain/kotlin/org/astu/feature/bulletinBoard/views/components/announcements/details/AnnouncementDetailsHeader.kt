package org.astu.feature.bulletinBoard.views.components.announcements.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.astu.feature.bulletinBoard.views.components.announcements.common.AnnouncementAuthor
import org.astu.feature.bulletinBoard.views.components.announcements.common.PublicationMoment
import org.astu.feature.bulletinBoard.views.components.announcements.common.ViewersCount

@Composable
fun AnnouncementDetailsHeader(
    authorName: String,
    publicationMoment: String,
    viewed: Int,
    viewedPercent: Int,
    audienceSize: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.wrapContentWidth()
        ) {
            AnnouncementAuthor(
                author = authorName,
            )
            PublicationMoment(
                moment = publicationMoment,
                textAlign = TextAlign.Start,
            )
        }
        ViewersCount(
            viewed = viewed,
            audienceSize = audienceSize,
            viewedPercent = viewedPercent,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}