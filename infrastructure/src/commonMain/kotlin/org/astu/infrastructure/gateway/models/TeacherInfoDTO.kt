package org.astu.infrastructure.gateway.models

import kotlinx.serialization.Serializable

@Serializable
class TeacherInfoDTO(val role: String, val title: String)