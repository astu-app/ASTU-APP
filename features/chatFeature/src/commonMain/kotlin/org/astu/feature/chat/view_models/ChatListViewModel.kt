package org.astu.feature.chat.view_models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.astu.feature.chat.ChatRepository
import org.astu.feature.chat.entities.Chat
import org.astu.feature.chat.screens.ChatScreen
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.StateScreenModel
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ChatListViewModel : StateScreenModel<ChatListViewModel.State>(State.Init) {
    sealed class State {
        data object Init : State()
        data object Loading : State()
        data class Error(val message: String) : State()
        data object ShowList : State()
        data object ShowChat : State()
    }

    sealed class DialogState {
        data object None : DialogState()
        data object Create : DialogState()
    }

    private val _dialogState = mutableStateOf<DialogState>(DialogState.None)
    val dialogState: androidx.compose.runtime.State<DialogState>
        get() = _dialogState

    private val repository by GlobalDIContext.inject<ChatRepository>()

    private val job: Job

    private val _chats = MutableStateFlow(mutableListOf<Chat>())
    val chats = _chats.asStateFlow()

    var chatScreen: MutableState<ChatScreen?> = mutableStateOf(null)


    init {
        mutableState.value = State.Loading
        job = screenModelScope.launch {
            updateChat {
                println(it)
                mutableState.value = State.Error(it.message ?: "Упс, ошибочка")
            }
            delay(5.toDuration(DurationUnit.SECONDS))

            while (isActive) {
                if (mutableState.value == State.ShowList) {
                    updateChat()
                }
                delay(5.toDuration(DurationUnit.SECONDS))
            }
        }
    }

    fun dialogDismiss() {
        _dialogState.value = DialogState.None
    }

    override fun onDispose() {
        job.cancel()
    }

    fun openCreateChatDialog() {
        _dialogState.value = DialogState.Create
    }

    fun selectChat(chat: Chat) = screenModelScope.launch {
        chatScreen.value = ChatScreen(chat) {
            chatScreen.value = null
            mutableState.value = State.ShowList
        }
        mutableState.value = State.ShowChat

    }

    private suspend fun updateChat(onFail: ((Throwable) -> Unit)? = null) {
        val result = runCatching {
            repository.getAllChat()
        }.onSuccess { newChats ->
            _chats.update { chatsToUpdate ->
                chatsToUpdate.addAll(newChats)
                chatsToUpdate.distinctBy { it.id }.toMutableList()
            }
            mutableState.value = State.ShowList
        }
        onFail?.let { result.onFailure(it) }
    }

    fun addChat(name: String) = screenModelScope.launch {
        runCatching {
            repository.createChat(name)
        }.onSuccess { id ->
            _chats.update {
                it.plus(Chat(id, name, listOf(), mutableListOf())).toMutableList()
            }
            dialogDismiss()
        }.onFailure {
            println(it)
        }
    }
}