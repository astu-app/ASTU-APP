package org.astu.feature.bulletinBoard.views.screens.announcements

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.feature.bulletinBoard.viewModels.announcements.DelayedHiddenAnnouncementsViewModel
import org.astu.feature.bulletinBoard.views.components.AnnouncementFeed
import org.astu.feature.bulletinBoard.views.components.announcements.summary.DelayedHiddenAnnouncementSummary
import org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent.DelayedHiddenAnnouncementDropdownMenuContent
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent
import org.astu.feature.bulletinBoard.views.screens.announcements.actions.AnnouncementDetailsScreen
import org.astu.feature.bulletinBoard.views.screens.announcements.actions.EditAnnouncementScreen
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

class DelayedHiddenAnnouncementsScreen(private val onReturn: () -> Unit) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { DelayedHiddenAnnouncementsViewModel(onReturn) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Объявления с отложенным сокрытием") },
                    navigationIcon = {
                        IconButton(onClick = onReturn) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        // Кнопка обновить
                        IconButton(onClick = { viewModel.loadAnnouncements() },) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = "Обновить список объявлений с отложенным сокрытием",
                                tint = CurrentColorScheme.primary
                            )
                        }
                    }
                )
            },
        ) {
            Surface(
                modifier = Modifier.padding(top = it.calculateTopPadding())
            ) {
                val state by viewModel.state.collectAsState()
                when (state) {
                    DelayedHiddenAnnouncementsViewModel.State.Loading -> Loading()
                    DelayedHiddenAnnouncementsViewModel.State.LoadingDone -> AnnouncementFeed(mapAnnouncements(viewModel))
                    DelayedHiddenAnnouncementsViewModel.State.LoadingAnnouncementsError -> showErrorDialog(viewModel)
                    DelayedHiddenAnnouncementsViewModel.State.DeletingAnnouncement -> { }
                    DelayedHiddenAnnouncementsViewModel.State.DeletingAnnouncementError -> showErrorDialog(viewModel)
                }
            }

            if (viewModel.showErrorDialog) {
                ActionFailedDialog(
                    label = viewModel.errorDialogLabel,
                    body = viewModel.errorDialogBody,
                    onTryAgainRequest = viewModel.onErrorDialogTryAgainRequest,
                    onDismissRequest = viewModel.onErrorDialogDismissRequest,
                    showDismissButton = true
                )
            }
        }
    }

    private fun showErrorDialog(viewModel: DelayedHiddenAnnouncementsViewModel) {
        viewModel.showErrorDialog = true
    }

    private fun mapAnnouncements(viewModel: DelayedHiddenAnnouncementsViewModel): List<@Composable () -> Unit> =
        viewModel.content.map {
            {
                DelayedHiddenAnnouncementSummary(
                    content = it,
                    announcementDropDown = { pressOffset, showDropDown ->
                        announcementDropDown(
                            it,
                            pressOffset,
                            showDropDown,
                            viewModel
                        )
                    }
                )
            }
        }

    @Composable
    private fun announcementDropDown(announcement: AnnouncementSummaryContent, pressOffset: DpOffset, showDropDown: MutableState<Boolean>, viewModel: DelayedHiddenAnnouncementsViewModel) {
        var dropdownHeight by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        val navigator = LocalNavigator.currentOrThrow

        val dropdownMenuContent = remember {
            createDropdownMenuContent(
                openInfoScreen = { navigator.push(AnnouncementDetailsScreen(announcement.id) { navigator.pop() }) },
                openEditScreen = { navigator.push(EditAnnouncementScreen(announcement.id) { navigator.pop() }) },
                delete = { viewModel.delete(announcement.id) },
            )
        }
        // при pressOffset == DpOffset.Zero левый верхний угол меню совпадает с верхним левым углом карты объявления
        DropdownMenu(
            expanded = showDropDown.value,
            onDismissRequest = { showDropDown.value = false },
            offset = pressOffset.copy(y = pressOffset.y + dropdownHeight),
            modifier = Modifier.onSizeChanged { dropdownHeight = with(density) { it.height.toDp() } }
        ) {
            dropdownMenuContent.items.forEach { item ->
                if (item.icon != null) {
                    DropdownMenuItem(
                        text = { Text(item.name) },
                        onClick = {
                            showDropDown.value = false
                            item.onClick()
                        },
                        leadingIcon = { Icon(item.icon, null) }
                    )
                } else {
                    DropdownMenuItem(
                        text = {
                            Text(item.name)
                            showDropDown.value = false
                        },
                        onClick = item.onClick,
                    )
                }
            }
        }
    }

    private fun createDropdownMenuContent(
        openInfoScreen: () -> Unit,
        openEditScreen: () -> Unit,
        delete: () -> Unit,
    ): DelayedHiddenAnnouncementDropdownMenuContent {
        return DelayedHiddenAnnouncementDropdownMenuContent(
            onInfoClick = openInfoScreen,
            onEditClick = openEditScreen,
            onDeleteClick = delete,
        )
    }
}