package org.astu.app.screens.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import org.astu.app.theme.CurrentColorScheme

class ChatScreen(private val channel: Channel, private val onReturn: () -> Unit) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = { Text(channel.name) },
                    navigationIcon = {
                        IconButton(onClick = onReturn) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                )
            }
        ) {
            Column(Modifier.fillMaxSize()) {
                MessageList(Modifier.weight(1f))
                MessageInput()
            }
        }

    }

    @Composable
    fun MessageList(modifier: Modifier) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                reverseLayout = true, // 5
            ) {
                items(channel.messages) { message ->
                    MessageCard(message) // 6
                }
            }
        }
    }

    @Composable
    fun MessageCard(messageItem: Message) { // 1
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = when { // 2
                messageItem.isMine -> Alignment.End
                else -> Alignment.Start
            },
        ) {
            Card(
                modifier = Modifier.widthIn(max = 340.dp),
                shape = cardShapeFor(messageItem),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        messageItem.isMine -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.secondary
                    }
                ),
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = messageItem.text,
                    color = when {
                        messageItem.isMine -> MaterialTheme.colorScheme.onPrimary
                        else -> MaterialTheme.colorScheme.onSecondary
                    },
                )
            }
            Text(
                // 4
                text = messageItem.who,
                fontSize = 12.sp,
            )
        }
    }

    @Composable
    fun cardShapeFor(message: Message): Shape {
        val roundedCorners = RoundedCornerShape(16.dp)
        return when {
            message.isMine -> roundedCorners.copy(bottomEnd = CornerSize(0))
            else -> roundedCorners.copy(bottomStart = CornerSize(0))
        }
    }

    @Composable
    fun MessageInput() {
        val inputValue = remember { mutableStateOf("") }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier.height(56.dp),
                onClick = { },
            ) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = null
                )
            }
            TextField(
                modifier = Modifier.weight(1f),
                value = inputValue.value,
                onValueChange = { inputValue.value = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions { },
                placeholder = { Text("Сообщение") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CurrentColorScheme!!.surface,
                    unfocusedContainerColor = CurrentColorScheme!!.surface,
                    focusedIndicatorColor = CurrentColorScheme!!.surface,
                    unfocusedIndicatorColor = CurrentColorScheme!!.surface
                )
            )
            IconButton(
                modifier = Modifier.height(56.dp),
                onClick = { },
                enabled = inputValue.value.isNotBlank(),
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null
                )
            }
        }
    }
}