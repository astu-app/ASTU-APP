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
package org.astu.feature.single_window.client.models

import kotlinx.serialization.Serializable


/**
 * 
 * @param name 
 * @param description 
 * @param type 
 * @param value 
 */
@Serializable
data class RequirementFieldDTO (

    val name: String,
    val description: String,
    val type: String,
    val value: String
)