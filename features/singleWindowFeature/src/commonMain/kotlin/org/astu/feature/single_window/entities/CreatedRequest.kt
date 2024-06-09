package org.astu.feature.single_window.entities

import kotlinx.datetime.Instant

class CreatedRequest(
    val id: String,
    val name: String,
    val description: String,
    val fields: List<CreatedRequirementField<Any>>,
    val date: Instant
)