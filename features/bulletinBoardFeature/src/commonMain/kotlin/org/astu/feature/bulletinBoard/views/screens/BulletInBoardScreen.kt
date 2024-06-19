package org.astu.feature.bulletinBoard.views.screens

import BulletInBoardViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.astu.feature.bulletinBoard.views.components.AnnouncementFeed
import org.astu.feature.bulletinBoard.views.components.announcements.summary.PublishedAnnouncementSummary
import org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent.AuthorDropdownMenuContent
import org.astu.feature.bulletinBoard.views.components.announcements.summary.dropdownMenuContent.PostedAnnouncementDropdownMenuContentBase
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent
import org.astu.feature.bulletinBoard.views.screens.announcements.DelayedHiddenAnnouncementsScreen
import org.astu.feature.bulletinBoard.views.screens.announcements.DelayedPublishedAnnouncementsScreen
import org.astu.feature.bulletinBoard.views.screens.announcements.HiddenAnnouncementsScreen
import org.astu.feature.bulletinBoard.views.screens.announcements.actions.AnnouncementDetailsScreen
import org.astu.feature.bulletinBoard.views.screens.announcements.actions.CreateAnnouncementScreen
import org.astu.feature.bulletinBoard.views.screens.announcements.actions.EditAnnouncementScreen
import org.astu.feature.bulletinBoard.views.screens.userGroups.UserGroupsScreen
import org.astu.infrastructure.components.ActionFailedDialog
import org.astu.infrastructure.components.Loading
import org.astu.infrastructure.theme.CurrentColorScheme

class BulletInBoardScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel { BulletInBoardViewModel() }

        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Объявления") },
                    actions = {
                        // Кнопка обновить
                        IconButton(onClick = { viewModel.loadAnnouncements() }) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
                                contentDescription = "Обновить список опубликованных объявлений",
                                tint = CurrentColorScheme.primary
                            )
                        }

                        // Выпадающее меню
                        TopAppBarMenuActions()
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val createAnnouncementScreen = CreateAnnouncementScreen { navigator.pop() }
                        navigator.push(createAnnouncementScreen)
                    },
                    containerColor = CurrentColorScheme.tertiaryContainer,
                ) {
                    Icon(Icons.Outlined.Add, "Создать объявление")
                }
            },
        ) {
            Surface(
                modifier = Modifier.padding(top = it.calculateTopPadding())
            ) {
                val state by viewModel.state.collectAsState()
                when (state) {
                    BulletInBoardViewModel.State.Loading -> { hideErrorDialog(viewModel); Loading() }
                    BulletInBoardViewModel.State.LoadingDone -> { hideErrorDialog(viewModel); AnnouncementFeed(mapAnnouncements(viewModel)) }
                    BulletInBoardViewModel.State.LoadingAnnouncementsError -> showErrorDialog(viewModel)
                    BulletInBoardViewModel.State.StoppingSurvey -> hideErrorDialog(viewModel)
                    BulletInBoardViewModel.State.StoppingSurveyError -> showErrorDialog(viewModel)
                    BulletInBoardViewModel.State.HidingAnnouncement -> hideErrorDialog(viewModel)
                    BulletInBoardViewModel.State.HidingAnnouncementError -> showErrorDialog(viewModel)
                    BulletInBoardViewModel.State.DeletingAnnouncement -> hideErrorDialog(viewModel)
                    BulletInBoardViewModel.State.DeletingAnnouncementError -> showErrorDialog(viewModel)
                    else -> hideErrorDialog(viewModel)
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



    @Composable
    private fun TopAppBarMenuActions() {
        val navigator = LocalNavigator.currentOrThrow
        var expanded by remember { mutableStateOf(false) }

        Box {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Показать меню экрана доски объявлений")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = !expanded }
            ) {
                Text(
                    "Скрытые объявления",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clickable {
                            val hiddenAnnouncementsScreen = HiddenAnnouncementsScreen { navigator.pop() }
                            navigator.push(hiddenAnnouncementsScreen)
                        }
                        .padding(5.dp)
                )
                Text(
                    "Объявления с отложенным сокрытием",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clickable {
                            val delayedHiddenAnnouncementsScreen = DelayedHiddenAnnouncementsScreen { navigator.pop() }
                            navigator.push(delayedHiddenAnnouncementsScreen)
                        }
                        .padding(5.dp)
                )
                Text(
                    "Объявления с отложенной публикацией",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clickable {
                            val delayedPublishedAnnouncementsScreen = DelayedPublishedAnnouncementsScreen { navigator.pop() }
                            navigator.push(delayedPublishedAnnouncementsScreen)
                        }
                        .padding(5.dp)
                )

                HorizontalDivider(Modifier.padding(horizontal = 5.dp))
                Text(
                    "Группы пользователей",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth()
                        .clickable {
                            val userGroupsScreen = UserGroupsScreen { navigator.pop() }
                            navigator.push(userGroupsScreen)
                        }
                        .padding(5.dp)
                )

//                HorizontalDivider(Modifier.padding(horizontal = 5.dp)) // todo восстановить отображение пункта с категориями объявлений
//                Text(
//                    "Категории объявлений",
//                    fontSize = 18.sp,
//                    modifier = Modifier
//                        .padding(5.dp)
//                        .fillMaxWidth()
//                        .clickable { Logger.d("Пункт меню \"Категории объявлений\" выбран") }
//                        .padding(5.dp)
//                )
            }
        }
    }

    private fun showErrorDialog(viewModel: BulletInBoardViewModel) {
        viewModel.showErrorDialog = true
    }

    private fun hideErrorDialog(viewModel: BulletInBoardViewModel) {
        viewModel.showErrorDialog = false
    }

    private fun mapAnnouncements(viewModel: BulletInBoardViewModel): List<@Composable () -> Unit> =
        viewModel.content.map {
            {
                PublishedAnnouncementSummary(
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
    private fun announcementDropDown(announcement: AnnouncementSummaryContent, pressOffset: DpOffset, showDropDown: MutableState<Boolean>, viewModel: BulletInBoardViewModel) {
        var dropdownHeight by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current

        val navigator = LocalNavigator.currentOrThrow

        val dropdownMenuContent = remember {
            createDropdownMenuContent(
                openInfoScreen = { navigator.push(AnnouncementDetailsScreen(announcement.id) { navigator.pop() }) },
                openEditScreen = { navigator.push(EditAnnouncementScreen(announcement.id) { navigator.pop() }) },
                closeSurvey = { viewModel.closeSurveys(announcement.getClosableSurveyIds()) },
                hide = { viewModel.hide(announcement.id) },
                delete = { viewModel.delete(announcement.id) },
                containsOpenSurveys = announcement.containsClosableSurveys()
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
        closeSurvey: () -> Unit,
        hide: () -> Unit,
        delete: () -> Unit,
        containsOpenSurveys: Boolean
    ): PostedAnnouncementDropdownMenuContentBase {
        val onCloseSurvey = if (!containsOpenSurveys) null else closeSurvey

        return AuthorDropdownMenuContent(
            onInfoClick = openInfoScreen,
            onEditClick = openEditScreen,
            onStopSurveyClick = onCloseSurvey,
            onHideClick = hide,
            onDeleteClick = delete,
        )
    }
}