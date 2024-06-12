package org.astu.feature.single_window.entities

import kotlinx.datetime.Instant
import org.astu.infrastructure.JavaSerializable

class CreatedRequest(
    val id: String,
    val name: String,
    val description: String,
    val fields: List<CreatedRequirementField<Any>>,
    val date: Instant
): JavaSerializable