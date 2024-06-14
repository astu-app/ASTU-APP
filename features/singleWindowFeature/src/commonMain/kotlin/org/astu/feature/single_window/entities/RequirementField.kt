package org.astu.feature.single_window.entities

import kotlinx.serialization.Serializable
import org.astu.infrastructure.JavaSerializable

@Serializable
class RequirementField(val requirement: Requirement, var value: String): JavaSerializable

