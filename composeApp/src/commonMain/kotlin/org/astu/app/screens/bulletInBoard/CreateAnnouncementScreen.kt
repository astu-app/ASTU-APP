package org.astu.app.screens.bulletInBoard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.astu.app.components.bulletinBoard.announcements.creation.AnnouncementCreator
import org.astu.app.theme.CurrentColorScheme

class CreateAnnouncementScreen(private val onReturn: () -> Unit) : Screen {
    private val creator: AnnouncementCreator = AnnouncementCreator()




    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 4.dp)
                        ) {
                            Text(
                                text = "Новое объявление",
                                modifier = Modifier.weight(1f)
                            )

                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.weight(0.7f)
                            ) {
                                Button(
                                    enabled = creator.canCreate(),
                                    onClick = { TODO("Создание объявления еще не реализовано") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CurrentColorScheme.surface,
                                        disabledContainerColor = CurrentColorScheme.surface,
                                        contentColor = CurrentColorScheme.primary,
                                    )
                                ) {

                                    Text(
                                        text = "Создать",
                                        style = MaterialTheme.typography.titleLarge,
                                    )
                                }
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onReturn) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Покинуть экран просмотра деталей объявления"
                            )
                        }
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) {
            val scrollState = rememberScrollState()
            LaunchedEffect(Unit) { scrollState.animateScrollTo(100) }

            Surface(
                modifier = Modifier
                    .padding(top = it.calculateTopPadding())
                    .verticalScroll(scrollState)
            ) {
                creator.Content()
            }
        }
    }
}