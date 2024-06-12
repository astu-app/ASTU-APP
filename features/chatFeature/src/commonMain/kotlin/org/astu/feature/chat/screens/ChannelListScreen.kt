package org.astu.feature.chat.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import io.ktor.http.*
import org.astu.feature.chat.entities.Chat
import org.astu.feature.chat.view_models.ChatListViewModel
import org.astu.infrastructure.SerializableScreen

class ChannelListScreen : SerializableScreen {
//    private val channels = listOf(
//        Channel("Название чата", listOf(Message("Азим", "Привет", true), Message("Бот", "Тест"))),
//        Channel("Другое название чата", listOf(Message("Бот", "Тест"))),
//        Channel("Пустой чат", listOf())
//    )

    private lateinit var viewModel: ChatListViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { ChatListViewModel() }
        val dialogState by remember { viewModel.dialogState }

        when (dialogState) {
            ChatListViewModel.DialogState.Create -> CreateChatDialog()
            ChatListViewModel.DialogState.None -> {}
        }
        ShowScene()
    }

    @Composable
    fun ShowScene() {
        val state by remember { viewModel.state }
        val chatScreen by remember { viewModel.chatScreen }
        when (state) {
            is ChatListViewModel.State.Error -> TopBarOfChat {
                Error(
                    Modifier.padding(it),
                    (state as ChatListViewModel.State.Error).message
                )
            }

            ChatListViewModel.State.Init -> TODO()
            ChatListViewModel.State.Loading -> TopBarOfChat { Loading(Modifier.padding(it)) }
            ChatListViewModel.State.ShowChat -> chatScreen?.Content()
            ChatListViewModel.State.ShowList -> TopBarOfChat { ChatList(Modifier.padding(it)) }
        }

    }

    @Composable
    fun CreateChatDialog() {
        Dialog(
            onDismissRequest = viewModel::dialogDismiss,
            DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = true)
        ) {
            Card(Modifier.padding(vertical = 40.dp, horizontal = 30.dp)) {
                var name by remember { mutableStateOf("") }
                Column(
                    Modifier.padding(vertical = 40.dp, horizontal = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(name, { name = it }, placeholder = { Text("Название чата") })
                    Button({ viewModel.addChat(name) }) {
                        Text("Создать")
                    }
                }
            }
        }
    }

    @Composable
    fun Loading(modifier: Modifier) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()

        }
    }

    @Composable
    fun Error(modifier: Modifier, message: String) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(message)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBarOfChat(content: @Composable (PaddingValues) -> Unit) {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar(
                { Text("Чаты", textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(viewModel::openCreateChatDialog) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                }
            )
        }, content = content)
    }

    @Composable
    fun ChatList(modifier: Modifier) {
        val chats = remember { viewModel.chats }.collectAsState()
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (chats.value.isNotEmpty())
                LazyColumn(Modifier.fillMaxSize()) {
                    items(chats.value) { chat ->
                        ChannelListItem(chat)
                        HorizontalDivider()
                    }
                }
            else
                Text("У вас нет чатов")
        }
    }

    @Composable
    fun ChannelListItem(chat: Chat) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable { viewModel.selectChat(chat) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(chat.name)
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = chat.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                )

                val lastMessageText = chat.messages.lastOrNull()?.run { "${member.name}: $text" } ?: ""
                Text(
                    text = lastMessageText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }

    private fun String.toHslColor(
        saturation: Float = 0.5f,
        lightness: Float = 0.4f
    ): Color {
        var hue = this.getOrElse(0) { ' ' }.code.toFloat() * this.hashCode() * this.length
        hue = (hue % 360).coerceAtLeast(0.0f)
        return Color.hsl(hue, saturation, lightness)
    }

    @Composable
    fun Avatar(
        name: String,
        modifier: Modifier = Modifier,
        size: Dp = 40.dp,
    ) {
        Box(modifier.size(size), contentAlignment = Alignment.Center) {
            val color = name.toHslColor()
            val initials = name.first().uppercase()
            Image(
                painter = ColorPainter(color),
                modifier = Modifier.clip(CircleShape),
                contentDescription = "Avatar"
            )
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(SolidColor(color))
            }
            Text(text = initials, color = Color.White)
        }
    }
}