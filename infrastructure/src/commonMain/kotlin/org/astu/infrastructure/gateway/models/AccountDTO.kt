package org.astu.infrastructure.gateway.models

import kotlinx.serialization.Serializable

@Serializable
data class AccountDTO(
    val id: String,
    val firstName: String,
    val secondName: String,
    val patronymic: String? = null,
    val isEmployee: Boolean = false,
    val isStudent: Boolean = false,
    val isTeacher: Boolean = false,
    val isAdmin: Boolean = false,
    val employeeInfo: EmployeeInfoDTO? = null,
    val studentInfo: StudentInfoDTO? = null,
    val teacherInfo: TeacherInfoDTO? = null
)