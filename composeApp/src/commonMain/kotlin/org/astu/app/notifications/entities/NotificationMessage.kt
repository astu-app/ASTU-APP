package org.astu.app.notifications.entities

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.astu.infrastructure.AnySerializer
import org.astu.infrastructure.JavaSerializable

/**
 * The MessageExternal holds information about a message which was sent by an Application.
 */
@Serializable
class NotificationMessage : JavaSerializable {
    /**
     * The application id that send this message.
     */
    val appid: Long? = null

    private val date: Instant? = null

    var extras: MutableMap<String, @Serializable(with = AnySerializer::class) Any>? = null

    /**
     * The message id.
     */
    val id: Long? = null

    /**
     * The message. Markdown (excluding html) is allowed.
     */
    var message: String? = null

    /**
     * The priority of the message.
     */
    var priority: Long? = null

    /**
     * The title of the message.
     */
    var title: String? = null


    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || this::class != other::class) {
            return false
        }
        val message = other as NotificationMessage
        return this.appid == message.appid && (this.date == message.date) && (this.extras == message.extras) && (this.id == message.id) && (this.message == message.message) && (this.priority == message.priority) && (this.title == message.title)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("class Message {\n")

        sb.append("    appid: ").append(toIndentedString(appid)).append("\n")
        sb.append("    date: ").append(toIndentedString(date)).append("\n")
        sb.append("    extras: ").append(toIndentedString(extras)).append("\n")
        sb.append("    id: ").append(toIndentedString(id)).append("\n")
        sb.append("    message: ").append(toIndentedString(message)).append("\n")
        sb.append("    priority: ").append(toIndentedString(priority)).append("\n")
        sb.append("    title: ").append(toIndentedString(title)).append("\n")
        sb.append("}")
        return sb.toString()
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private fun toIndentedString(o: Any?): String {
        if (o == null) {
            return "null"
        }
        return o.toString().replace("\n", "\n    ")
    }

    override fun hashCode(): Int {
        var result = appid?.hashCode() ?: 0
        result = 31 * result + (date?.hashCode() ?: 0)
        result = 31 * result + (extras?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + (priority?.hashCode() ?: 0)
        result = 31 * result + (title?.hashCode() ?: 0)
        return result
    }
}




