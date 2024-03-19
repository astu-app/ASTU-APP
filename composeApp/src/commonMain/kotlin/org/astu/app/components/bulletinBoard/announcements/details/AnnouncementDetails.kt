package org.astu.app.components.bulletinBoard.announcements.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.astu.app.components.TreeDropDown
import org.astu.app.components.bulletinBoard.announcements.details.models.AnnouncementDetailsContent
import org.astu.app.components.bulletinBoard.attachments.Attachment
import org.astu.app.theme.CurrentColorScheme

@Composable
fun AnnouncementDetails(
    details: AnnouncementDetailsContent,
    modifier: Modifier = Modifier
        .fillMaxSize()
        .padding(
            top = 8.dp,
            bottom = 8.dp,
            end = 16.dp,
            start = 16.dp
        )
) {
    Column(modifier = modifier) {
        AnnouncementDetailsHeader(
            authorName = details.author,
            publicationMoment = details.publicationTime,
            viewed = details.viewed,
            viewedPercent = details.viewedPercent,
            audienceSize = details.audienceSize
        )

        Text(
            text = details.text,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp)
        )

        AddDivider()

        details.attachments?.forEach {attachment ->
            Attachment(attachment)
        }

        AddDivider()

        TreeDropDown(details.rootAudienceNode) {
            Text(
                text = "Получатели",
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            )
        }
    }
}

@Composable
private inline fun AddDivider() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        thickness = 1.dp,
        color = CurrentColorScheme?.outlineVariant ?: Color.Gray
    )
}