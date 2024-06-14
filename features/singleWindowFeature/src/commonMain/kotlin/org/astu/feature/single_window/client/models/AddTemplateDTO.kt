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
import org.astu.infrastructure.JavaSerializable

/**
 * 
 * @param name 
 * @param description 
 * @param category 
 * @param departmentId 
 * @param requirements 
 * @param groups 
 */
@Serializable
data class AddTemplateDTO (

    val name: String,
    val description: String,
    val category: String,
    val departmentId: String,
    val requirements: List<AddRequirementDTO>,
    val groups: List<Groups>
): JavaSerializable {
    /**
    * 
    * Values: STUDENT,EMPLOYEE,GRADUATE
    */
    @Serializable
    enum class Groups(val value: String){
        Student("Student"),
        Employee("Employee"),
        GRADUATE("GRADUATE");
    }
}