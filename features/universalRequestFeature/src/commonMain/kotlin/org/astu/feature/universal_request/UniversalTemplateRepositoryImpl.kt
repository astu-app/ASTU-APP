package org.astu.feature.universal_request

import org.astu.feature.universal_request.client.TemplateApi
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.client.models.TemplateFieldDTO
import org.astu.feature.universal_request.client.models.TemplateInfo
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.GatewayServiceConfig
import org.astu.infrastructure.JavaSerializable

class UniversalTemplateRepositoryImpl : UniversalTemplateRepository, JavaSerializable {
    private val config by GlobalDIContext.inject<GatewayServiceConfig>()
    private val client = TemplateApi(config.url)

    override suspend fun getAllTemplates(): List<TemplateDTO> {
        return client.getAllTemplates()
    }

    override suspend fun createTemplate(info: TemplateInfo, filename: String, bytes: ByteArray): String {
        client.upload(info, filename, bytes)
        return "OK"
    }

    override suspend fun fillTemplate(templateDTO: TemplateDTO, fields: List<TemplateFieldDTO>): ByteArray {
        return client.fillTemplate(templateDTO.id, fields)
    }
}