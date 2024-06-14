package org.astu.feature.single_window.entities

import org.astu.feature.single_window.client.models.AddRequestDTO
import org.astu.infrastructure.JavaSerializable

class Request(
    val template: Template,
    val type: AddRequestDTO.Type,
    val email: String? = null,
    val fields: List<RequirementField>
): JavaSerializable