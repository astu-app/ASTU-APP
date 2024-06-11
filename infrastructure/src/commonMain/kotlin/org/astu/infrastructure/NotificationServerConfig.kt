package org.astu.infrastructure

import kotlinx.serialization.Serializable

@Serializable
class NotificationServerConfig(val host: String, val clientId: Long, val clientToken: String)