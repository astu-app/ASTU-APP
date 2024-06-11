package org.astu.app.notifications.entities

import kotlinx.serialization.Serializable

@Suppress("unused")
@Serializable
class NotificationMessageList(val paging: Paging, val messages: List<NotificationMessage>)