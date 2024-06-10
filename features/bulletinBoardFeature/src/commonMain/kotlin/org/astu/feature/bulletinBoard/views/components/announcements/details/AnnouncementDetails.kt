package org.astu.feature.bulletinBoard.views.components.announcements.details

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.components.attachments.Attachment
import org.astu.feature.bulletinBoard.views.entities.announcement.details.AnnouncementDetailsContent
import org.astu.feature.bulletinBoard.views.entities.users.UserToViewMappers.toViews
import org.astu.infrastructure.components.dropdown.DropDown
import org.astu.infrastructure.theme.CurrentColorScheme

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
            hidingMoment = details.hidingTime,
            delayedPublicationMoment = details.delayedPublicationTime,
            delayedHidingMoment = details.delayedHidingTime,
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

        if (!details.attachments.isNullOrEmpty()) {
            details.attachments.forEach { attachment ->
                Attachment(attachment)
            }
            AddDivider()
        }

        DropDown(
            details.audience.toViews()
        ) {
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
private fun AddDivider() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        thickness = 1.dp,
        color = CurrentColorScheme.outlineVariant
    )
}
