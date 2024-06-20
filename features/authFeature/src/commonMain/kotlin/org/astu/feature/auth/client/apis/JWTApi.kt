/**
 * AuthService API
 * Сервис авторизации пользователей
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package org.astu.feature.auth.client.apis

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.astu.feature.auth.client.models.JWTLoginDTO
import org.astu.infrastructure.gateway.models.Tokens
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.exceptions.ApiException

class AuthApi(private val basePath: String = "/") {
    private val client by GlobalDIContext.inject<HttpClient>()


    /**
     *
     * Авторизация пользователя с помощью JWT
     * @param body
     * @return Tokens
     */
    suspend fun apiAuthJwtLoginPost(body: JWTLoginDTO): Tokens {
        val response = client.post("${basePath}/api/auth/jwt/login") {
            this.contentType(ContentType.Application.Json)
            setBody(body)
        }

        return when (response.status) {
            HttpStatusCode.OK -> response.body<Tokens>()
            HttpStatusCode.BadRequest -> throw ApiException(response.bodyAsText())
            else -> throw ApiException(response.bodyAsText())
        }
    }
}