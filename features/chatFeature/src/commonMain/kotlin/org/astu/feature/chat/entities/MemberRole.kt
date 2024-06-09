package org.astu.feature.chat.entities

import kotlinx.serialization.Serializable

@Serializable
enum class MemberRole(val value: String){
    Admin("admin"),
    User("user")
}