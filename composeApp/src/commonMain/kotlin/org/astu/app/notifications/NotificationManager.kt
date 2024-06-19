package org.astu.app.notifications

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.util.*
import io.ktor.websocket.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.astu.app.notifications.entities.NotificationMessage
import org.astu.app.notifications.entities.NotificationMessageList
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.NotificationServerConfig
import org.astu.infrastructure.security.IAccountSecurityManager
import kotlin.random.Random

object NotificationManager: JavaSerializable {
    private val config by GlobalDIContext.inject<NotificationServerConfig>()
    private val client: HttpClient by GlobalDIContext.inject<HttpClient>()

    private val accountSecurityManager by GlobalDIContext.inject<IAccountSecurityManager>()


    @OptIn(InternalAPI::class)
    suspend fun loadExistingNotifications() {
        runCatching {
            val response = client.get("http://${config.host}/application/${config.clientId}/message?token=${config.clientToken}")
            Logger.d(response.content.readUTF8Line(Int.MAX_VALUE) ?: "no content", tag = "notifications")

            val notifications = response.body<NotificationMessageList>()
            notifications.messages.forEach {
                handleMessage(it)
            }
        }

    }

    suspend fun connect() {
        while (accountSecurityManager.hasAccess()) {
            try {
                client.webSocket("ws://${config.host}/stream?token=${config.clientToken}") {
                    for (frame in incoming) {
                        if (frame is Frame.Text) {
                            val receivedText = frame.readText()
                            val message = Json.decodeFromString<NotificationMessage>(receivedText)
                            handleMessage(message)
                        }
                    }
                }
            } catch (e: SocketTimeoutException) {
                Logger.w("socket timeout", e, tag = "notifications")
            } catch (e: Exception) {
                Logger.e("socket exception", e, tag = "notifications")
            }
        }
    }

    fun disconnect() {
        client.close()
    }



    @OptIn(DelicateCoroutinesApi::class)
    private fun handleMessage(message: NotificationMessage) {
        val userId = (message.extras?.get("receiver::options") as? JsonElement)
            ?.jsonObject
            ?.get("user_id")
            ?.jsonPrimitive
            ?.content

        // если user_id отсутствует, уведомление предназначается всем пользователям - показываем его
        if (userId != null && userId != accountSecurityManager.currentUserId)
            return

        showNotification(message.id ?: Random.nextLong(), message.title ?: "Уведомление", message.message ?: "")
        Logger.i("Received notification:\n${message.title}\n${message.message}", tag = "notifications")

        GlobalScope.launch {
            client.delete("http://${config.host}/message/${message.id}?token=${config.clientToken}")
        }
    }
}