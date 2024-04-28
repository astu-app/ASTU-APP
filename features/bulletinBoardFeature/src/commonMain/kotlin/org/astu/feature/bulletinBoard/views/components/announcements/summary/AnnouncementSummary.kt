package org.astu.feature.bulletinBoard.views.components.announcements.summary

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import org.astu.feature.bulletinBoard.viewModels.humanization.humanizeDateTime
import org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent.AuthorDropdownMenuContent
import org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent.DropdownMenuContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.Attachment
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent
import org.astu.feature.bulletinBoard.views.screens.announcementAction.AnnouncementDetailsScreen
import org.astu.feature.bulletinBoard.views.screens.announcementAction.EditAnnouncementScreen
import org.astu.infrastructure.theme.CurrentColorScheme

@OptIn(FormatStringsInDatetimeFormats::class)
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

    var showDropdownMenu by remember { mutableStateOf(false) }
    var pressOffset by remember { mutableStateOf(DpOffset.Zero) }
    var dropdownHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

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
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = {
                            val detailsScreen = AnnouncementDetailsScreen(content.id) { navigator.pop() }
                            navigator.push(detailsScreen)
                        },
                        onLongPress = {
                            pressOffset = DpOffset(it.x.toDp(), it.y.toDp())
                            showDropdownMenu = true
                        }
                    )
                }
        ) {
            Column {
                val publicationMoment by remember {
                    mutableStateOf(humanizeDateTime(content.publicationTime))
                }
                AnnouncementHeader(content.author, publicationMoment, Modifier.fillMaxWidth())
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
                AnnouncementFooter(content.viewed, content.audienceSize)
            }
        }

        val dropdownMenuContent = remember {
            createDropdownMenuContent(
                openInfoScreen = { navigator.push(AnnouncementDetailsScreen(content.id) { navigator.pop() }) },
                openEditScreen = { navigator.push(EditAnnouncementScreen(content.id) { navigator.pop() }) },
            )
        }
        // при pressOffset == DpOffset.Zero левый верхний угол меню совпадает с верхним левым углом карты объявления
        DropdownMenu(
            expanded = showDropdownMenu,
            onDismissRequest = { showDropdownMenu = false },
            offset = pressOffset.copy(y = pressOffset.y + dropdownHeight),
            modifier = Modifier.onSizeChanged { dropdownHeight = with(density) { it.height.toDp() } }
        ) {
            dropdownMenuContent.items.forEach { item ->
                if (item.icon != null) {
                    DropdownMenuItem(
                        text = { Text(item.name) },
                        onClick = {
                            showDropdownMenu = false
                            item.onClick()
                        },
                        leadingIcon = { Icon(item.icon, null) }
                    )
                } else {
                    DropdownMenuItem(
                        text = {
                            Text(item.name)
                            showDropdownMenu = false
                        },
                        onClick = item.onClick,
                    )
                }
            }
        }
    }
}

private fun createDropdownMenuContent(
    openInfoScreen: () -> Unit,
    openEditScreen: () -> Unit,
): DropdownMenuContentBase {
    return AuthorDropdownMenuContent(
        onInfoClick = {
            Logger.log(Severity.Info, "Dropdown", null, "On Info Click")
            openInfoScreen()
        },
        onEditClick = {
            Logger.log(Severity.Info, "Dropdown", null, "On Edit Click")
            openEditScreen()
        },
        onStopSurveyClick = { Logger.log(Severity.Info, "Dropdown", null, "On StopSurvey Click") },
        onHideClick = { Logger.log(Severity.Info, "Dropdown", null, "On Hide Click") },
        onDeleteClick = { Logger.log(Severity.Info, "Dropdown", null, "On Delete Click") },
    )
}
