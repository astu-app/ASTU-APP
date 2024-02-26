package org.astu.app.screens.chat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen

data class Channel(val name: String, val messages: List<Message>)

data class Message(val who: String, val text: String, val isMine: Boolean = false)

class ChannelListScreen : Screen {
    private val channels = listOf(
        Channel("Название чата", listOf(Message("Азим", "Привет", true), Message("Бот", "Тест"))),
        Channel("Другое название чата", listOf(Message("Бот", "Тест"))),
        Channel("Пустой чат", listOf())
    )

    private var chat: MutableState<ChatScreen?> = mutableStateOf(null)

    @Composable
    override fun Content() {
        if (chat.value != null)
            chat.value!!.Content()
        else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                if (/*Loading*/false) {
                    CircularProgressIndicator()
                } else {
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(channels) { channel ->
                            ChannelListItem(channel)
                            Divider()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ChannelListItem(channel: Channel) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth().clickable {
                    chat.value = ChatScreen(channel) { chat.value = null }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(channel.name)
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = channel.name,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                )

                val lastMessageText = channel.messages.lastOrNull()?.run { "$who: $text" } ?: ""
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