package org.astu.feature.single_window

import org.astu.feature.single_window.entities.CreatedRequest
import org.astu.feature.single_window.entities.Request
import org.astu.feature.single_window.entities.RequirementType
import org.astu.feature.single_window.entities.Template

interface SingleWindowRepository {
    suspend fun getTemplates(): List<Template>

    suspend fun getRequests(): List<CreatedRequest>

    suspend fun saveTemplate(template: Template)

    suspend fun sendRequest(request: Request)

    suspend fun getRequirementTypes(): List<RequirementType>

    suspend fun makeAddRequest(template: Template)
}