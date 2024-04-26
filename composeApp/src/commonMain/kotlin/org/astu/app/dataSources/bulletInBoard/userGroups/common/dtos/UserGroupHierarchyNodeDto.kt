package org.astu.app.dataSources.bulletInBoard.userGroups.common.dtos

import kotlinx.serialization.Serializable

/**
 * Объект описывает положение группы пользователей в иерархии групп пользователей
 */
@Serializable
data class UserGroupHierarchyNodeDto(val id: String, val children: List<UserGroupHierarchyNodeDto>)