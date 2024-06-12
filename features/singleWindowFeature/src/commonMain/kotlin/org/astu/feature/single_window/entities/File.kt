package org.astu.feature.single_window.entities

import kotlinx.serialization.Serializable
import org.astu.infrastructure.JavaSerializable

@Serializable
class File(val path: String? = null): JavaSerializable