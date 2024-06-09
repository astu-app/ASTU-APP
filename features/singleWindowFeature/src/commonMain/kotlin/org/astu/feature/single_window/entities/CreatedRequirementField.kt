package org.astu.feature.single_window.entities

import kotlinx.serialization.Serializable

@Serializable
class CreatedRequirementField<T>(
    val name: String,
    val description: String,
    val type: String,
    val value: T
)