package org.astu.feature.bulletinBoard.views.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import co.touchlab.kermit.Logger
import org.astu.feature.bulletinBoard.viewModels.announcements.BulletInBoardViewModel
import org.astu.feature.bulletinBoard.views.components.BulletInBoard
import org.astu.feature.bulletinBoard.views.screens.announcementAction.CreateAnnouncementScreen
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
                                        .clickable { Logger.d("Пункт меню \"Скрытые объявления\" выбран") }
                                        .padding(5.dp)
                                )
                                Text(
                                    "Объявления с отложенным сокрытием",
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .clickable { Logger.d("Пункт меню \"Объявления с отложенным сокрытием\" выбран") }
                                        .padding(5.dp)
                                )
                                Text(
                                    "Объявления с отложеной публикацией",
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .clickable { Logger.d("Пункт меню \"Объявления с отложеной публикацией\" выбран") }
                                        .padding(5.dp)
                                )

                                HorizontalDivider(Modifier.padding(horizontal = 5.dp))
                                Text(
                                    "Группы пользователей",
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .clickable { Logger.d("Пункт меню \"Группы пользователей\" выбран") }
                                        .padding(5.dp)
                                )

                                HorizontalDivider(Modifier.padding(horizontal = 5.dp))
                                Text(
                                    "Категории объявлений",
                                    fontSize = 18.sp,
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth()
                                        .clickable { Logger.d("Пункт меню \"Категории объявлений\" выбран") }
                                        .padding(5.dp)
                                )
                            }
                        }
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
                    Icon(Icons.Outlined.Edit, null)
                }
            },
        ) {
            Surface(
                modifier = Modifier.padding(top = it.calculateTopPadding())
            ) {
                val state by viewModel.state.collectAsState()
                when (state) {
                    BulletInBoardViewModel.State.Loading -> Loading()
                    BulletInBoardViewModel.State.LoadingDone -> BulletInBoard(viewModel.content)
                    BulletInBoardViewModel.State.Error -> showErrorDialog(viewModel)
                }
            }

            if (viewModel.showErrorDialog.value) {
                ActionFailedDialog(
                    label = viewModel.errorDialogLabel.value,
                    body = viewModel.errorDialogBody.value,
                    onTryAgainRequest = {
                        viewModel.loadAnnouncements()
                        viewModel.showErrorDialog.value = false
                    },
                    showDismissButton = false
                )
            }
        }
    }



    private fun showErrorDialog(viewModel: BulletInBoardViewModel) {
        viewModel.showErrorDialog.value = true
    }
}