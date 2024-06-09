package org.astu.feature.single_window.entities

import kotlinx.serialization.Serializable

@Serializable
class Requirement(
    val id: String,
    val requirementType: String,
    val name: String,
    val description: String,
//    val isMandatory: Boolean
)