package org.astu.feature.chat.entities

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,
    val text: String,
    val member: Member,
    var attachments: List<String>,
    var isDelivered: Boolean = false,
    val isMine: Boolean = false
)