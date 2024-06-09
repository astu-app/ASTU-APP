package org.astu.feature.chat.entities

import kotlinx.serialization.Serializable

@Serializable
class Chat(val id: String, val name: String, var members: List<Member>, var messages: MutableList<Message>)

