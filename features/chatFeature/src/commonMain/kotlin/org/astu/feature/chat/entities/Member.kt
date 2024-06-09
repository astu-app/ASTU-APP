package org.astu.feature.chat.entities

import kotlinx.serialization.Serializable

@Serializable
class Member(val id: String, val name: String, var isMuted: Boolean = false, var role: MemberRole, var itMe: Boolean = false)