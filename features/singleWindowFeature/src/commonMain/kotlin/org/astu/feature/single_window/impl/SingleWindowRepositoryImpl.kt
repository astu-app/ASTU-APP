package org.astu.feature.single_window.impl

import kotlinx.serialization.StringFormat
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.astu.feature.single_window.SingleWindowRepository
import org.astu.feature.single_window.client.RequestApi
import org.astu.feature.single_window.client.models.*
import org.astu.feature.single_window.entities.*
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.GatewayServiceConfig
import org.astu.infrastructure.JavaSerializable

class SingleWindowRepositoryImpl : SingleWindowRepository, JavaSerializable {
    private val config by GlobalDIContext.inject<GatewayServiceConfig>()

    private val api = RequestApi(config.url)

    override suspend fun getTemplates(): List<Template> {
        return api.apiRequestServiceTemplateGet().map { dto ->
            println("Шаблон: ${dto}")
            println("Количество полей ${dto.requirements.count()}")
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

    override suspend fun getUserRequests(): List<CreatedRequest> {
        return api.apiRequestServiceUserRequestGet().map { dto ->
            CreatedRequest(
                dto.id,
                dto.name,
                dto.description,
                dto.fields.map { CreatedRequirementField(it.name, it.description, it.type, it.value) },
                dto.createdAt,
                dto.type,
                dto.status,
                dto.message
            )
        }
    }

    override suspend fun failRequest(id: String, body: FailRequestDTO) {
        return api.apiRequestServiceEmployeeRequestIdFailPost(body, id)
    }

    override suspend fun removeRequest(id: String) {
        return api.apiRequestServiceUserRequestIdDelete(id)

    }

    override suspend fun successRequest(id: String, filename: String, body: ByteArray) {
        api.apiRequestServiceEmployeeRequestIdSuccessPost(id, filename, body)
    }

    override suspend fun successRequest(id: String, comment: String) {
        api.apiRequestServiceEmployeeRequestIdSuccessPost(id, comment)
    }

    override suspend fun getEmployeeRequests(): List<EmployeeCreatedRequest> {
        return api.apiRequestServiceEmployeeRequestGet().map { dto ->
            EmployeeCreatedRequest(
                dto.id,
                dto.name,
                dto.description,
                dto.fields.map { CreatedRequirementField(it.name, it.description, it.type, it.value) },
                dto.userInfo!!,
                dto.createdAt,
                dto.type
            )
        }
    }

    override suspend fun saveTemplate(template: AddTemplateDTO) {
        api.apiRequestServiceTemplatePost(template)
    }

    override suspend fun sendRequest(request: Request) {
        val json = Json
        val fields = request.fields.map {
            AddRequirementFieldDTO(it.requirement.id, json.encodeToString(it.value))
        }

        val addDTO = AddRequestDTO(
            request.template.id,
            request.type,
            if (request.email.isNullOrBlank()) null else request.email,
            fields
        )
        val id = api.apiRequestServiceUserRequestPost(addDTO)
    }

    override suspend fun getRequirementTypes(): List<RequirementType> {
        return api.apiRequestServiceRequirementTypesGet().map { dto ->
            RequirementType(dto.id, dto.name)
        }
    }

    override suspend fun makeAddRequest(template: Template): Request {
        return Request(template, AddRequestDTO.Type.FaceToFace, "",
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

    private fun getField(field: Requirement): RequirementField {
        return when (field.requirementType.uppercase()) {
            "STRING" -> createAddStringField(field)
            "FILE" -> createAddFileField(field)
            else -> throw Exception("Unsupported requirement type")
        }
    }

    private fun createAddStringField(field: Requirement): RequirementField {
        return RequirementField(field, "")
    }

    private fun createAddFileField(field: Requirement): RequirementField {
        return RequirementField(field, "")
    }

    private fun createdStringField(field: RequirementFieldDTO): CreatedRequirementField<Any> {
        return CreatedRequirementField(field.name, field.description, field.type, "")
    }

    private fun createdFileField(field: RequirementFieldDTO): CreatedRequirementField<Any> {
        return CreatedRequirementField(field.name, field.description, field.type, File())
    }

    private fun serializeField(formatter: StringFormat, field: RequirementField): String {
        return when (field.requirement.requirementType.uppercase()) {
            "STRING" -> formatter.encodeToString(field.value as String)
            "FILE" -> formatter.encodeToString(field.value as File)
            else -> throw Exception("Unsupported requirement type")
        }
    }
}