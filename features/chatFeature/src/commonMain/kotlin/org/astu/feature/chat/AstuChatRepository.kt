package org.astu.feature.chat

import kotlinx.datetime.Instant
import org.astu.feature.chat.client.ChatApi
import org.astu.feature.chat.client.models.*
import org.astu.feature.chat.entities.Chat
import org.astu.feature.chat.entities.Member
import org.astu.feature.chat.entities.MemberRole
import org.astu.feature.chat.entities.Message
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.GatewayServiceConfig
import org.astu.infrastructure.IAccountRepository
import org.astu.infrastructure.gateway.models.SummaryAccountDTO

class AstuChatRepository : ChatRepository {
    private val config by GlobalDIContext.inject<GatewayServiceConfig>()
    private val client = ChatApi(config.url)
    private val accountRepository by GlobalDIContext.inject<IAccountRepository>()
//    private val chatIdToMembers: MutableMap<String, MutableList<Member>> = mutableMapOf()
    private val chats: MutableMap<String, Chat> = mutableMapOf()

    override suspend fun getAllChat(): Array<Chat> = client.apiChatGet().map {
        it.toChat()
    }.onEach { chat ->
        chats[chat.id] = chat
    }.toTypedArray()

    override suspend fun getChat(id: String): Chat = client.apiChatIdGet(id).toChat()
        .also {
            chats[it.id] = it
        }

    override suspend fun getMembers(id: String): Array<Member> {
        val members = client.apiChatIdUserGet(id).map { it.toMember() }
//        chatIdToMembers[id] = members.toMutableList()
        return members.toTypedArray()
    }

    override suspend fun getMember(chatId: String): Member {
//        val userInfo = accountRepository.getUserInfo()
//        var member = chatIdToMembers[chatId]?.filter { it.id == userInfo.userId }
//        if (member != null && member.any())
//            return member.first()
//        getMembers(chatId)
//        member = chatIdToMembers[chatId]?.filter { it.id == userInfo.userId }
//        if (member != null && member.any())
//            return member.first()
        TODO()
    }

    override suspend fun getMessagesByPage(
        chatId: String,
        page: Int,
        elements: Int?
    ): List<Message> =
        client.apiChatIdMsgPageGet(chatId, page, elements).map { it.toMessage(chatId) }

    override suspend fun getMessagesByDatetime(chatId: String, datetime: Instant): List<Message> =
        client.apiChatIdMsgTimeGet(chatId, datetime).map { it.toMessage(chatId) }

    override suspend fun sendMassage(chatId: String, message: Message): String =
        client.apiChatIdMsgPost(
            AddMessageDTO(message.text, message.attachments.toTypedArray()),
            chatId
        )

    override suspend fun addMember(chatId: String, member: Member) {
        println(member.id)
        client.apiChatIdUserPost(AddUserDTO(member.id), chatId)
        println(member.id)
    }

    override suspend fun createChat(name: String): String = client.apiChatPost(AddChatDTO(name))

    override suspend fun findMembers(name: String): List<SummaryAccountDTO> {
        return client.apiFindUsersGet(name)
    }

    private suspend fun ChatDTO.toChat() =
        Chat(
            id,
            name,
            members.map { it.toMember() },
            messages.map { it.toMessage(id) }.toMutableList()
        )

    private suspend fun MessageDTO.toMessage(chatId: String) =
        Message(
            id = id,
            text = text,
            member = chats[chatId]!!.members.first { it.id == this@toMessage.userId },
            attachments = attachment.toList(),
            isDelivered = true,
            isMine = accountRepository.getUserInfo().userId == this@toMessage.userId
        )

    private suspend fun ChatMemberDTO.toMember(): Member {
//        val userInfo = accountRepository.getUserInfo(this.id)
        return Member(
            id = this.id,
            name = "${this.secondName} ${this.firstName.getOrElse(0) { ' ' }}",
            role = MemberRole.entries.first { this.role == it.value },
            itMe = this.itMe
        )
    }
}