package org.astu.feature.single_window.entities

import kotlinx.datetime.Instant
import org.astu.feature.single_window.client.models.RequestDTO
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.gateway.models.AccountDTO

class EmployeeCreatedRequest(
    val id: String,
    val name: String,
    val description: String,
    val fields: List<CreatedRequirementField<Any>>,
    val userInfo: AccountDTO,
    val date: Instant,
    val type: RequestDTO.Type
): JavaSerializable