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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import org.astu.app.components.bulletinBoard.announcements.creation.AnnouncementCreator
import org.astu.app.theme.CurrentColorScheme

class CreateAnnouncementScreen(private val onReturn: () -> Unit) : Screen {
//    private val creator: MutableState<AnnouncementCreator> = mutableStateOf(AnnouncementCreator())
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
//                                    enabled = creator.value.canCreate(),
                                    enabled = creator.canCreate(),
                                    onClick = { TODO("Создание объявления еще не реализовано") },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = CurrentColorScheme?.surface ?: Color.White,
                                        disabledContainerColor = CurrentColorScheme?.surface ?: Color.White,
                                        contentColor = CurrentColorScheme?.primary ?: Color.Black,
                                    )
                                ) {
//                                    Logger.log(Severity.Info, "", null, creator.value.canCreate().toString())
                                    Logger.log(Severity.Info, "", null, creator.canCreate().toString())

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
//                creator.value.Content()
                creator.Content()
            }
        }
    }
}