package org.astu.feature.auth.jwtDecoding

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun decodeBase64(base64Str: String): String {
    val decodedBytes = Base64.decode(base64Str)
    return decodedBytes.decodeToString()
}