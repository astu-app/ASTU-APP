package org.astu.feature.single_window.impl

import kotlinx.serialization.StringFormat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.astu.feature.single_window.SingleWindowRepository
import org.astu.feature.single_window.client.RequestApi
import org.astu.feature.single_window.client.models.AddRequestDTO
import org.astu.feature.single_window.client.models.AddRequirementFieldDTO
import org.astu.feature.single_window.client.models.RequirementFieldDTO
import org.astu.feature.single_window.entities.CreatedRequest
import org.astu.feature.single_window.entities.CreatedRequirementField
import org.astu.feature.single_window.entities.File
import org.astu.feature.single_window.entities.Request
import org.astu.feature.single_window.entities.Requirement
import org.astu.feature.single_window.entities.RequirementField
import org.astu.feature.single_window.entities.RequirementType
import org.astu.feature.single_window.entities.Template
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.GatewayServiceConfig

class SingleWindowRepositoryImpl : SingleWindowRepository {
    private val config by GlobalDIContext.inject<GatewayServiceConfig>()

    private val api = RequestApi(config.url)

    override suspend fun getTemplates(): List<Template> {
        return api.apiRequestServiceTemplateGet().map { dto ->
            Template(
                dto.id,
                dto.name,
                dto.description,
                dto.category,
                dto.requirements.map {
                    Requirement(
                        it.id,
                        it.requirementType,
                        it.name,
                        it.description
                    )
                })
        }
    }

    override suspend fun getRequests(): List<CreatedRequest> {
        return api.apiRequestServiceUserRequestGet().map { dto ->
            CreatedRequest(
                dto.id,
                dto.name,
                dto.description,
                dto.fields.map { getCreatedField(it) },
                dto.createdAt
            )
        }
    }

    override suspend fun saveTemplate(template: Template) {
//        val dto = AddTemplateDTO(template.name, template.description, template.category, template.)
//        api.apiRequestServiceTemplatePost()
        TODO("Not yet implemented")
    }

    override suspend fun sendRequest(request: Request) {
        val json = Json
        val fields = request.fields.map {
            AddRequirementFieldDTO(it.requirement.id, json.encodeToString(it.value))
        }

        AddRequestDTO(
            request.template.id,
            request.type,
            if (request.email.isNullOrBlank()) null else request.email,
            fields
        )
    }

    override suspend fun getRequirementTypes(): List<RequirementType> {
        return api.apiRequestServiceRequirementTypesGet().map { dto ->
            RequirementType(dto.id, dto.name)
        }
    }

    override suspend fun makeAddRequest(template: Template) {
        Request(template, AddRequestDTO.Type.FACETOFACE, "",
            template.requirements.map {
                getField(it)
            })

    }

    private fun getCreatedField(field: RequirementFieldDTO): CreatedRequirementField<Any> {
        return when (field.type.uppercase()) {
            "STRING" -> createdStringField(field)
            "FILE" -> createdFileField(field)
            else -> throw Exception("Unsupported requirement type")
        }
    }

    private fun getField(field: Requirement): RequirementField<Any> {
        return when (field.requirementType.uppercase()) {
            "STRING" -> createAddStringField(field)
            "FILE" -> createAddFileField(field)
            else -> throw Exception("Unsupported requirement type")
        }
    }

    private fun createAddStringField(field: Requirement): RequirementField<Any> {
        return RequirementField(field, "")
    }

    private fun createAddFileField(field: Requirement): RequirementField<Any> {
        return RequirementField(field, File())
    }

    private fun createdStringField(field: RequirementFieldDTO): CreatedRequirementField<Any> {
        return CreatedRequirementField(field.name, field.description, field.type, "")
    }

    private fun createdFileField(field: RequirementFieldDTO): CreatedRequirementField<Any> {
        return CreatedRequirementField(field.name, field.description, field.type, File())
    }

    private fun serializeField(formatter: StringFormat, field: RequirementField<Any>): String {
        return when (field.requirement.requirementType.uppercase()) {
            "STRING" -> formatter.encodeToString(field.value as String)
            "FILE" -> formatter.encodeToString(field.value as File)
            else -> throw Exception("Unsupported requirement type")
        }
    }
}