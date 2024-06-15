package org.astu.feature.universal_request.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.*
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.append
import io.ktor.http.contentType
import io.ktor.util.toByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.astu.feature.universal_request.client.models.TemplateDTO
import org.astu.feature.universal_request.client.models.TemplateFieldDTO
import org.astu.feature.universal_request.client.models.TemplateInfo
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable
import org.astu.infrastructure.exceptions.ApiException

class TemplateApi(private val baseUrl: String) : JavaSerializable {
    //    private val securityHttpClient by GlobalDIContext.inject<SecurityHttpClient>()
    private val client by GlobalDIContext.inject<HttpClient>()
//    private val client = securityHttpClient.instance


    suspend fun upload(info: TemplateInfo, filename: String, array: ByteArray) {
        if (!filename.endsWith(".doc") && !filename.endsWith(".docx")) {
            throw RuntimeException()
        }
        val response = client.submitFormWithBinaryData(url = "${baseUrl}api/uni-request-service/templates/upload",
            formData = formData {
                append("file", array, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=$filename")
                })
                val form = Json.encodeToString(info)
                append("info", form, Headers.build {
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                })
            }
        )
        println(response.status)
        println(response.bodyAsText())
        return when (response.status) {
            HttpStatusCode.OK -> response.body<Unit>()
            HttpStatusCode.BadRequest -> throw ApiException("Некорректный запрос")
            else -> throw ApiException("Проблема с интернет соединением")
        }
    }

    suspend fun getAllTemplates(): List<TemplateDTO> {
        val response = client.get("${baseUrl}api/uni-request-service/templates")

        return when (response.status) {
            HttpStatusCode.OK -> response.body<List<TemplateDTO>>()
            HttpStatusCode.BadRequest -> throw ApiException("Некорректный запрос")
            else -> throw ApiException("Проблема с интернет соединением")
        }
    }

    suspend fun fillTemplate(templateId: String, fields: List<TemplateFieldDTO>): ByteArray {
        println("template id $templateId")
        val response = client.post("${baseUrl}api/uni-request-service/templates/${templateId}") {
            contentType(ContentType.Application.Json)
            setBody(fields)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.bodyAsChannel().toByteArray()
            HttpStatusCode.BadRequest -> throw ApiException("Некорректный запрос")
            else -> throw ApiException("Проблема с интернет соединением")
        }
    }
}