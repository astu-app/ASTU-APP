package org.astu.feature.auth.jwtDecoding

import kotlinx.serialization.json.Json

fun decodeJwtPayload(token: String): JwtPayload? {
    val parts = token.split(".")
    if (parts.size != 3) {
        return null
    }

    val payload = decodeBase64(parts[1])
    val jsonPayload = Json.decodeFromString<JwtPayload>(payload)
    return jsonPayload
}
