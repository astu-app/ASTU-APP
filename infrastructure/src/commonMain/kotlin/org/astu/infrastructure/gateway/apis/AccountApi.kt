/**
 * gateway API
 * gateway API
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package org.astu.infrastructure.gateway.apis

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.SecurityHttpClient
import org.astu.infrastructure.gateway.models.AccountDTO

class AccountApi(private val baseUrl: String = "/") {
    private val securityHttpClient by GlobalDIContext.inject<SecurityHttpClient>()
    private val client = securityHttpClient.instance

    /**
     * Получение информации об аккаунте
     * @return AccountDTO
     */
    suspend fun apiAccountGet(): AccountDTO {
        val response = client.get("${baseUrl}api/account-service/me")
        println(response.status)
        println(response)
        return when (response.status) {
            HttpStatusCode.OK -> response.body<AccountDTO>()
            else -> throw RuntimeException()
        }
    }
}
