package org.astu.feature.universal_request

import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.client.models.TemplateFieldDTO
import org.astu.feature.universal_request.client.models.TemplateInfo
import org.astu.infrastructure.JavaSerializable

interface UniversalTemplateRepository: JavaSerializable {
    suspend fun getAllTemplates(): List<TemplateDTO>
    suspend fun createTemplate(info: TemplateInfo, filename: String, bytes: ByteArray): String
    suspend fun fillTemplate(templateDTO: TemplateDTO, fields: List<TemplateFieldDTO>): ByteArray
}