package org.astu.app.components.bulletinBoard.announcements.summary

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.app.components.bulletinBoard.announcements.common.AnnouncementAuthor
import org.astu.app.components.bulletinBoard.announcements.common.PublicationMoment

@Composable
fun AnnouncementHeader(author: String, publicationTime: String, modifier: Modifier = Modifier.fillMaxWidth()) {
    Column(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
            modifier = Modifier.fillMaxWidth()
        ) {
            AnnouncementAuthor(
                author = author,
                modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
            modifier = Modifier.fillMaxWidth()
        ) {
            PublicationMoment(
                moment = publicationTime,
                modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically)
            )
        }
    }
}