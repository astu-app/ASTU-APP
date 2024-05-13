package org.astu.feature.bulletinBoard.views.screens.announcements

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import org.astu.feature.bulletinBoard.viewModels.announcements.HiddenAnnouncementsViewModel
import org.astu.feature.bulletinBoard.views.components.AnnouncementFeed
import org.astu.feature.bulletinBoard.views.components.announcements.summary.AnnouncementSummary
import org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent.HiddenAnnouncementDropdownMenuContent
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent
import org.astu.feature.bulletinBoard.views.screens.announcements.actions.AnnouncementDetailsScreen
import org.astu.feature.bulletinBoard.views.screens.announcements.actions.EditAnnouncementScreen
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading

class HiddenAnnouncementsScreen(private val onReturn: () -> Unit) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { HiddenAnnouncementsViewModel(onReturn) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Скрытые объявления") },
                    navigationIcon = {
                        IconButton(onClick = onReturn) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, null)
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
                    HiddenAnnouncementsViewModel.State.Loading -> Loading()
                    HiddenAnnouncementsViewModel.State.LoadingDone -> AnnouncementFeed(mapAnnouncements(viewModel))
                    HiddenAnnouncementsViewModel.State.LoadingAnnouncementsError -> showErrorDialog(viewModel)
                    HiddenAnnouncementsViewModel.State.DeletingAnnouncement -> { }
                    HiddenAnnouncementsViewModel.State.DeletingAnnouncementError -> showErrorDialog(viewModel)
                    HiddenAnnouncementsViewModel.State.RestoringAnnouncement -> { }
                    HiddenAnnouncementsViewModel.State.RestoringAnnouncementError -> showErrorDialog(viewModel)
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

    private fun showErrorDialog(viewModel: HiddenAnnouncementsViewModel) {
        viewModel.showErrorDialog = true
    }

    private fun mapAnnouncements(viewModel: HiddenAnnouncementsViewModel): List<@Composable () -> Unit> =
        viewModel.content.map {
            {
                AnnouncementSummary(
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
    private fun announcementDropDown(announcement: AnnouncementSummaryContent, pressOffset: DpOffset, showDropDown: MutableState<Boolean>, viewModel: HiddenAnnouncementsViewModel) {
        var dropdownHeight by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        val navigator = LocalNavigator.currentOrThrow

        val dropdownMenuContent = remember {
            createDropdownMenuContent(
                openInfoScreen = { navigator.push(AnnouncementDetailsScreen(announcement.id) { navigator.pop() }) },
                restore = { viewModel.restore(announcement.id) },
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
        restore: () -> Unit,
        openEditScreen: () -> Unit,
        delete: () -> Unit,
    ): HiddenAnnouncementDropdownMenuContent {
        return HiddenAnnouncementDropdownMenuContent(
            onInfoClick = openInfoScreen,
            onRestore = restore,
            onEditClick = openEditScreen,
            onDeleteClick = delete,
        )
    }
}