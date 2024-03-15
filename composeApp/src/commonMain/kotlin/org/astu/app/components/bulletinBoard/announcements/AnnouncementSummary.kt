package org.astu.app.components.bulletinBoard.announcements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.astu.app.components.bulletinBoard.announcements.footer.AnnouncementFooter
import org.astu.app.components.bulletinBoard.announcements.header.AnnouncementHeader
import org.astu.app.components.bulletinBoard.announcements.models.AnnouncementSummaryContent
import org.astu.app.components.bulletinBoard.attachments.Attachment
import org.astu.app.theme.CurrentColorScheme

@Composable
fun AnnouncementSummary(
    content: AnnouncementSummaryContent,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(
            vertical = 8.dp,
            horizontal = 16.dp
        ),
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CurrentColorScheme?.secondaryContainer ?: Color.Cyan,
        ),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
        ) {
            AnnouncementHeader(content.author, content.publicationTime, Modifier.fillMaxWidth())
            Text(
                text = content.text,
                lineHeight = 1.5.em,
                style = TextStyle(
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            if (content.attachments != null) {
                Divider(
                    thickness = 1.dp,
                    color = CurrentColorScheme?.outlineVariant ?: Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                content.attachments.forEach {
                    Attachment(it)
                }
            }
            AnnouncementFooter(content.viewed, content.audienceSize)
        }
    }
}