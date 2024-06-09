package org.astu.feature.chat.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import org.astu.feature.chat.entities.Chat
import org.astu.feature.chat.entities.Message
import org.astu.feature.chat.view_models.ChatViewModel
import org.astu.infrastructure.theme.CurrentColorScheme

class ChatScreen(private val chat: Chat, private val onReturn: () -> Unit) : Screen {
    private lateinit var viewModel: ChatViewModel

    @Composable
    override fun Content() {
        viewModel = remember { ChatViewModel(chat) }
        val dialogState by viewModel.dialogState.collectAsState()
        when (dialogState) {
            ChatViewModel.DialogState.AddUser -> AddUserDialog()
            ChatViewModel.DialogState.None -> {}
        }
        ShowScene()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowScene() {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = { Text(chat.name) },
                    navigationIcon = {
                        IconButton(onClick = onReturn) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {
                        MenuButton()
                    }
                )
            }
        ) {
            Column(Modifier.fillMaxSize()) {
                MessageList(Modifier.weight(1f))
                HorizontalDivider()
                MessageInput()
            }
        }
    }

    @Composable
    fun MessageList(modifier: Modifier) {
        val listState = rememberLazyListState()
        val chat = remember { viewModel.chat }
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                reverseLayout = true,
                state = listState
            ) {
                items(chat.value.messages, key = { it.id }) { message ->
                    MessageCard(message)
                }
            }
            val firstVisibleIndex = listState.firstVisibleItemIndex
            val visibleItemsCount = listState.layoutInfo.visibleItemsInfo.size
            if (firstVisibleIndex + visibleItemsCount > 0) {

            }
        }
    }

    @Composable
    fun MessageCard(messageItem: Message) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = when {
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
                text = messageItem.member.name,
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
        val inputValue = remember { viewModel.text }
        Row(verticalAlignment = Alignment.CenterVertically) {
//            IconButton(
//                modifier = Modifier.height(56.dp),
//                onClick = { },
//            ) {
//                Icon(
//                    imageVector = Icons.Default.AttachFile,
//                    contentDescription = null
//                )
//            }
//            TODO(В следующий раз добавить эту фичу)
            TextField(
                modifier = Modifier.weight(1f),
                value = inputValue.value,
                onValueChange = { inputValue.value = it },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions { },
                placeholder = { Text("Сообщение") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CurrentColorScheme.surface,
                    unfocusedContainerColor = CurrentColorScheme.surface,
                    focusedIndicatorColor = CurrentColorScheme.surface,
                    unfocusedIndicatorColor = CurrentColorScheme.surface
                )
            )
            IconButton(
                modifier = Modifier.height(56.dp),
                onClick = viewModel::sendMessage,
                enabled = inputValue.value.isNotBlank(),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    fun MenuButton() {
        var expanded by remember { mutableStateOf(false) }
        Box {
            IconButton({ expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = null)
            }
            DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("Добавить пользователя") },
                    onClick = viewModel::openAddMemberDialog
                )
            }
        }
    }

    @Composable
    fun AddUserDialog() {
        Dialog(
            onDismissRequest = viewModel::dialogDismiss,
            DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = true)
        ) {
            Card(Modifier.fillMaxSize().padding(vertical = 40.dp, horizontal = 30.dp)) {
                var name by remember { mutableStateOf("") }
                val userInfoList by viewModel.currentUserInfoList.collectAsState()
                Column(
                    Modifier.fillMaxWidth().padding(vertical = 40.dp, horizontal = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TextField(name,
                        onValueChange = {
                            viewModel.getUsers(it)
                            name = it
                        },
                        placeholder = { Text("ФИО пользователя") })
                    LazyColumn(Modifier.padding(vertical = 20.dp)) {
                        items(userInfoList) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "${it.secondName} ${it.firstName} ${it.patronymic ?: ""}",
                                    textAlign = TextAlign.Center
                                )
                                Button({ viewModel.addMember(it.id) }) {
                                    Text("Добавить")
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}