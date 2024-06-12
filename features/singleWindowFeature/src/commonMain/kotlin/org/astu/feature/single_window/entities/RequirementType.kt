package org.astu.feature.single_window.entities

import kotlinx.serialization.Serializable
import org.astu.infrastructure.JavaSerializable

@Serializable
class RequirementType (
    val id: String,
    val name: String
): JavaSerializable