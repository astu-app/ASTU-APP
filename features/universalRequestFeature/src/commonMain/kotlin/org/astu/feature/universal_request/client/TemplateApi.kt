package org.astu.feature.universal_request.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.client.models.TemplateFieldDTO
import org.astu.feature.universal_request.client.models.TemplateInfo
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.SecurityHttpClient

class TemplateApi(private val baseUrl: String): JavaSerializable {
//    private val securityHttpClient by GlobalDIContext.inject<SecurityHttpClient>()
    private val client by GlobalDIContext.inject<HttpClient>()
//    private val client = securityHttpClient.instance


    suspend fun upload(info: TemplateInfo, filename: String, array: ByteArray) {
        if (!filename.endsWith(".doc") && !filename.endsWith(".docx")) {
            throw RuntimeException()
        }
        val response = client.post("${baseUrl}api/uni-request-service/templates/upload") {
            setBody(MultiPartFormDataContent(
                formData {
                    append("file", array, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=$filename")
                    })
                    FormPart("info", info, Headers.build {
                        append(HttpHeaders.ContentType, ContentType.Application.Json)
                    })
                }
            ))
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body<Unit>()
            else -> throw RuntimeException()
        }
    }

    suspend fun getAllTemplates(): List<TemplateDTO> {
        val response = client.get("${baseUrl}api/uni-request-service/templates")

        return when (response.status) {
            HttpStatusCode.OK -> response.body<List<TemplateDTO>>()
            else -> throw RuntimeException()
        }
    }

    suspend fun fillTemplate(templateId: String, fields: List<TemplateFieldDTO>): ByteArray {
        val response = client.post("${baseUrl}api/uni-request-service/templates/${templateId}") {
            contentType(ContentType.Application.Json)
            setBody(fields)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.bodyAsChannel().toByteArray()
            else -> throw RuntimeException()
        }
    }
}