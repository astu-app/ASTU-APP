package org.astu.feature.chat.client

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.astu.feature.chat.client.models.*
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.SecurityHttpClient
import org.astu.infrastructure.gateway.models.SummaryAccountDTO
import kotlin.collections.Map

class ChatApi(private val baseUrl: String) {
    private val securityHttpClient by GlobalDIContext.inject<SecurityHttpClient>()
    private val client = securityHttpClient.instance

    /**
     *
     * Получение всех чатов, где состоит авторизованный пользователь
     * @return kotlin.Array<Chat>
     */
    suspend fun apiChatGet(): List<ChatDTO> {
        val response = client.get("${baseUrl}api/chat-service/chats") {
        }
        return when (response.status) {
            HttpStatusCode.OK -> response.body<List<ChatDTO>>()
            else -> throw RuntimeException()
        }
    }

    /**
     *
     * Информация о чате
     * @param id
     * @return Chat
     */
    suspend fun apiChatIdGet(id: String): ChatDTO {
        val response = client.get("${baseUrl}api/chat-service/chats/$id")
        return when (response.status) {
            HttpStatusCode.OK -> response.body<ChatDTO>()
            else -> throw RuntimeException()
        }
    }

    /**
     *
     * Выдача последних сообщений постранично
     * @param id
     * @param page  (optional)
     * @param elements  (optional)
     * @return kotlin.Array<Message>
     */
    suspend fun apiChatIdMsgPageGet(
        id: String,
        page: Int,
        elements: Int? = null
    ): List<MessageDTO> {

        val response = client.get("${baseUrl}api/chat-service/chats/$id/messages/page") {
            if (elements != null)
                parameter("elements", elements)
            parameter("page", page)
        }
        return when (response.status) {
            HttpStatusCode.OK -> response.body<List<MessageDTO>>()
            else -> throw RuntimeException()
        }
    }

    /**
     *
     * Добавление сообщения
     * @param body
     * @param id
     * @return MapString
     */
    suspend fun apiChatIdMsgPost(body: AddMessageDTO, id: String): String {
        val response = client.post("${baseUrl}api/chat-service/chats/$id/messages") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> response.body<String>().removeSurrounding("\"")
            else -> throw RuntimeException()
        }
    }

    /**
     *
     * Выдача последних сообщений с последнего datetime
     * @param id
     * @param datetime  (optional)
     * @return kotlin.Array<Message>
     */
    suspend fun apiChatIdMsgTimeGet(id: String, datetime: Instant): List<MessageDTO> {
        val response = client.get("${baseUrl}api/chat-service/chats/$id/messages/time") {
            parameter("datetime", Json.encodeToString(datetime))
        }
        return when (response.status) {
            HttpStatusCode.OK -> response.body<List<MessageDTO>>()
            else -> throw RuntimeException()
        }
    }

    /**
     *
     * Добавление пользователя в чат
     * @param body
     * @param id
     * @return kotlin.Any
     */
    suspend fun apiChatIdUserPost(body: AddUserDTO, id: String) {
        val response = client.post("${baseUrl}api/chat-service/chats/$id/users") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(body)
        }
        if (response.status == HttpStatusCode.OK)
            return
        throw RuntimeException()
    }

    /**
     *
     * Создание нового чата авторизованным пользователем
     * @param body
     * @return MapString
     */
    suspend fun apiChatPost(body: AddChatDTO): String {
        val response = client.post("${baseUrl}api/chat-service/chats") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(body)
        }
        return when (response.status) {
            HttpStatusCode.OK -> response.body<String>().removeSurrounding("\"")
            else -> throw RuntimeException()
        }
    }

    /**
     *
     * Выдача последних сообщений постранично
     * @param id
     * @return kotlin.Array<ChatMemberDTO>
     */
    suspend fun apiChatIdUserGet(id: String): Array<ChatMemberDTO> {

        val response = client.get("${baseUrl}api/chat-service/chats/$id/users") {
        }
        return when (response.status) {
            HttpStatusCode.OK -> response.body<Array<ChatMemberDTO>>()
            else -> throw RuntimeException()
        }
    }

    /**
     *
     * Выдача найденных пользователей по ФИО
     * @param id
     * @return kotlin.Array<ChatMemberDTO>
     */
    suspend fun apiFindUsersGet(text: String): List<SummaryAccountDTO> {

        val response = client.get("${baseUrl}api/account-service/find") {
            parameter("u", text)
        }
        return when (response.status) {
            HttpStatusCode.OK -> response.body<List<SummaryAccountDTO>>()
            else -> throw RuntimeException()
        }
    }


}
