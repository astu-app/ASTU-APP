package org.astu.app.notifications.entities

import kotlinx.serialization.Serializable
import org.astu.infrastructure.JavaSerializable

@Suppress("unused")
@Serializable
class NotificationMessageList(val paging: Paging, val messages: List<NotificationMessage>): JavaSerializable