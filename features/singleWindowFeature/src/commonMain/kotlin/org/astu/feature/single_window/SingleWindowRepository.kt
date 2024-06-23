package org.astu.feature.single_window

import org.astu.feature.single_window.client.models.AddTemplateDTO
import org.astu.feature.single_window.client.models.FailRequestDTO
import org.astu.feature.single_window.entities.*

interface SingleWindowRepository {
    suspend fun getTemplates(): List<Template>

    suspend fun getUserRequests(): List<CreatedRequest>
    suspend fun getEmployeeRequests(): List<EmployeeCreatedRequest>

    suspend fun saveTemplate(template: AddTemplateDTO)

    suspend fun sendRequest(request: Request)
    suspend fun failRequest(id: String, body: FailRequestDTO)
    suspend fun removeRequest(id: String)
    suspend fun successRequest(id: String, filename: String, body: ByteArray)
    suspend fun successRequest(id: String, comment: String)

    suspend fun getRequirementTypes(): List<RequirementType>

    suspend fun makeAddRequest(template: Template): Request
}