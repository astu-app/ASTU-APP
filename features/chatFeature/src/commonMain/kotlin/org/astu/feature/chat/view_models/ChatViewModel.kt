package org.astu.feature.chat.view_models

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.astu.feature.chat.ChatRepository
import org.astu.feature.chat.entities.Chat
import org.astu.feature.chat.entities.Member
import org.astu.feature.chat.entities.MemberRole
import org.astu.feature.chat.entities.Message
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.IAccountRepository
import org.astu.infrastructure.StateScreenModel
import org.astu.infrastructure.gateway.models.SummaryAccountDTO
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class ChatViewModel(chat: Chat) : StateScreenModel<ChatViewModel.State>(State.Init) {
    private val repository by GlobalDIContext.inject<ChatRepository>()
    private val accountRepository by GlobalDIContext.inject<IAccountRepository>()

    private var job: Job

    sealed class State {
        data object Init : State()
        data object Loading : State()
        data class Error(val message: String) : State()
        data object ShowChat : State()
    }

    sealed class DialogState {
        data object None : DialogState()
        data object AddUser : DialogState()
    }

    private val _dialogState = MutableStateFlow<DialogState>(DialogState.None)
    val dialogState = _dialogState.asStateFlow()

    private val _chat = MutableStateFlow(chat)
    val chat = _chat.asStateFlow()
    val text = mutableStateOf("")

    private val _currentUserInfoList: MutableStateFlow<List<SummaryAccountDTO>> =
        MutableStateFlow(listOf())
    val currentUserInfoList = _currentUserInfoList.asStateFlow()

    init {
        mutableState.value = State.Loading
        job = screenModelScope.launch {
            _chat.update {
                repository.getChat(it.id)
            }

            while (isActive) {
                val messages = runCatching {
                    repository.getMessagesByPage(_chat.value.id, 0, 20)
                }.getOrNull()
                if (messages != null) {
                    _chat.update {
                        it.also {
                            it.messages.addAll(0, messages.reversed())
                            it.messages =
                                it.messages.distinctBy { message -> message.id }.toMutableList()
                        }
                    }
                }
                delay(20.toDuration(DurationUnit.SECONDS))
            }
        }
        mutableState.value = State.ShowChat
    }

    fun dialogDismiss() {
        _dialogState.update { DialogState.None }
        _currentUserInfoList.update {
            listOf()
        }
    }

    override fun onDispose() {
        job.cancel()
    }

    fun sendMessage() = screenModelScope.launch {
        val chatId = chat.value.id
        val me = chat.value.members.first { it.itMe }
        val message = Message("", text.value, me, listOf(), isDelivered = false, isMine = true)
        val messageId = repository.sendMassage(chatId, message)
        _chat.update {
            it.messages.add(0, message.copy(id = messageId))
            it
        }
        text.value = ""
    }

    fun getUsers(name: String) = screenModelScope.launch {
        runCatching {
//            accountRepository.getAllUserInfoByName(name)
            repository.findMembers(name)
        }.onSuccess { users ->
            _currentUserInfoList.update {
                users.filter { user -> _chat.value.members.none { it.id == user.id } }
            }
        }
    }

    private fun updateMembers() {
        _currentUserInfoList.update { users ->
            users.filter { user -> _chat.value.members.none { it.id == user.id } }
        }
    }

    fun addMember(userId: String) = screenModelScope.launch {
        repository.addMember(chat.value.id, Member(userId, "", false, MemberRole.User))
        updateMembers()
    }

    fun openAddMemberDialog() {
        _dialogState.update { DialogState.AddUser }
    }
}