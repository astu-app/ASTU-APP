package org.astu.feature.auth.client.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoDTO(val userId: String, val name: String)