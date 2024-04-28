package org.astu.feature.bulletinBoard.models.dataSoruces.userGroups.common.dtos

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

/**
 * Иерархия групп пользователей
 */
@Serializable
data class UserGroupHierarchyDto @OptIn(ExperimentalSerializationApi::class) constructor(
    /**
     * Плоский список групп пользователей, входящих в иерархию
     */
    @JsonNames("usergroups")
    val userGroups: List<UserGroupSummaryWithMembersDto>,

    /**
     * Список корней иерархии групп пользователей
     */
    val roots: List<UserGroupHierarchyNodeDto>
)

