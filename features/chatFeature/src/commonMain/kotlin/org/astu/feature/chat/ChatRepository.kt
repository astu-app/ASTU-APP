package org.astu.feature.chat

import kotlinx.datetime.Instant
import org.astu.feature.chat.entities.Chat
import org.astu.feature.chat.entities.Member
import org.astu.feature.chat.entities.Message
import org.astu.infrastructure.gateway.models.SummaryAccountDTO

interface ChatRepository {
    suspend fun getAllChat(): Array<Chat>
    suspend fun getChat(id: String): Chat
    suspend fun getMembers(id: String): Array<Member>
    suspend fun getMember(chatId: String): Member
    suspend fun getMessagesByPage(chatId: String, page: Int, elements: Int? = null): List<Message>
    suspend fun getMessagesByDatetime(chatId: String, datetime: Instant): List<Message>
    suspend fun sendMassage(chatId: String, message: Message): String
    suspend fun addMember(chatId: String, member: Member)
    suspend fun createChat(name: String): String
    suspend fun findMembers(name: String): List<SummaryAccountDTO>
}