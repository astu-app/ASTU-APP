package org.astu.feature.bulletinBoard.views.components.announcements.summary

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.feature.bulletinBoard.views.components.attachments.Attachment
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent
import org.astu.feature.bulletinBoard.views.screens.announcements.actions.AnnouncementDetailsScreen
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.security.IAccountSecurityManager
import org.astu.infrastructure.theme.CurrentColorScheme

/**
 * @param headerMoment Момент, отображенный в заголовке. Например, момент публикации, отложенного сокрытия и т.п. Будет отображен вместо любого из соответствующих свойств объекта [content]
 */
@Composable
fun AnnouncementSummary(
    content: AnnouncementSummaryContent,
    headerMoment: String,
    announcementDropDown: @Composable (DpOffset, MutableState<Boolean>) -> Unit,
    modifier: Modifier

) {
    val accountSecurityManager by GlobalDIContext.inject<IAccountSecurityManager>()
    val navigator = LocalNavigator.currentOrThrow

    val showDropdownMenu = remember { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = CurrentColorScheme.secondaryContainer,
        ),
        modifier = modifier,
    ) {
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier.padding(
                start = 16.dp,
                end = 16.dp,
                top = 10.dp,
                bottom = 10.dp
            )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            val detailsScreen = AnnouncementDetailsScreen(content.id) { navigator.pop() }
                            navigator.push(detailsScreen)
                        },
                        onLongPress = {
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            showDropdownMenu.value = true
                        }
                    )
                }
        ) {
            Column {
                AnnouncementHeader(content.author.fullName, headerMoment, Modifier.fillMaxWidth())
                Text(
                    text = content.text,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )

                if (content.attachments.isNotEmpty()) {
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        thickness = 1.dp,
                        color = CurrentColorScheme.outlineVariant
                    )

                    // файлы выводятся раньше опроса
                    content.attachments
                        .sortedBy { it.type }
                        .forEach { Attachment(it) }
                }

                if (accountSecurityManager.currentUserId == content.author.id.toString()) {
                    AnnouncementFooter(content.viewed, content.audienceSize)
                }
            }
        }

        announcementDropDown(pressOffset, showDropdownMenu)
    }
}