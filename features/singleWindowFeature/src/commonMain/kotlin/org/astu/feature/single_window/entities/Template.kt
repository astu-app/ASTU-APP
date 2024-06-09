package org.astu.feature.single_window.entities

import kotlinx.serialization.Serializable

@Serializable
class Template(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val requirements: List<Requirement> = listOf(),
)