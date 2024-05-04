package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos

import kotlinx.serialization.Serializable

/**
 * Объект описывает положение группы пользователей в иерархии групп пользователей
 */
@Serializable
data class UserGroupHierarchyNodeDto(val id: String, val children: MutableList<UserGroupHierarchyNodeDto>)