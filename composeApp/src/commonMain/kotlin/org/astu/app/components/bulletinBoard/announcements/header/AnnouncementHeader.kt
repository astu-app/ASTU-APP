package org.astu.app.components.bulletinBoard.announcements.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnnouncementHeader(author: String, publicationTime: String, modifier: Modifier = Modifier.fillMaxWidth()) {
    Column(
        modifier = modifier
    ) {
        HeaderRow(author)
        HeaderRow(publicationTime)
    }
}