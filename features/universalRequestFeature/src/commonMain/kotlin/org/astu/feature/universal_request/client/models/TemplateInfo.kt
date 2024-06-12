package org.astu.feature.universal_request.client.models

import kotlinx.serialization.Serializable
import org.astu.infrastructure.JavaSerializable

@Serializable
data class TemplateInfo(val name: String, val description: String): JavaSerializable