package org.astu.feature.auth.jwtDecoding

import kotlinx.serialization.Serializable

@Serializable
data class JwtPayload(
    val aud: String,
    val iss: String,
    val id: String
)