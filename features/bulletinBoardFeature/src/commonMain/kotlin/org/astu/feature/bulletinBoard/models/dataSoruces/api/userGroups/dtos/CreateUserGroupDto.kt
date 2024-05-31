package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos

import kotlinx.serialization.Serializable

/**
 * @param name Название группы пользователей
 * @param adminId Идентификатор администратора группы пользователей
 * @param members Идентификаторы участников группы пользователей
 * @param parentIds Идентификаторы групп пользователей, являющихся родителями создаваемой
 * @param childIds Идентификаторы групп пользователей, являющихся дочерними создаваемой
 */
@Serializable
data class CreateUserGroupDto (
    val name: String,
    val adminId: String?,
    val members: List<UserIdWithMemberRightsDto>,
    val parentIds: List<String>,
    val childIds: List<String>,
)