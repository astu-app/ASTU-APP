package org.astu.infrastructure.gateway.models

import kotlinx.serialization.Serializable


@Serializable
class SummaryAccountDTO(
    val id: String,
    val firstName: String,
    val secondName: String,
    val patronymic: String?
)