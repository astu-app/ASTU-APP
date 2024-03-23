package org.astu.app.components.bulletinBoard.announcements.summary

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.app.components.bulletinBoard.announcements.summary.models.AnnouncementSummaryContent
import org.astu.app.components.bulletinBoard.attachments.Attachment
import org.astu.app.screens.bulletInBoard.AnnouncementDetailsScreen
import org.astu.app.theme.CurrentColorScheme

@OptIn(ExperimentalFoundationApi::class)
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
    val navigator = LocalNavigator.currentOrThrow

    Card(
        colors = CardDefaults.cardColors(
            containerColor = CurrentColorScheme.secondaryContainer,
        ),
        modifier = modifier
            .combinedClickable(
                onClick = {
                    val detailsScreen = AnnouncementDetailsScreen { navigator.pop() }
                    navigator.push(detailsScreen)
                },
                onLongClick = { /* todo */ }
            )
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )

            if (content.attachments != null) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    thickness = 1.dp,
                    color = CurrentColorScheme.outlineVariant
                )

                // файлы выводятся раньше опроса
                content.attachments.sortedBy {
                    it.type
                }.forEach {
                    Attachment(it)
                }
            }
            AnnouncementFooter(content.viewed, content.audienceSize)
        }
    }
}