/**
 * ChatService API
 * ChatService API
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
package org.astu.feature.chat.client.models

import kotlinx.serialization.Serializable


/**
 *
 * @param id
 * @param role
 */
@Serializable
data class ChatMemberDTO(
    val id: String,
    val role: String,
    val firstName: String,
    val secondName: String,
    val patronymic: String?,
    val itMe: Boolean
)