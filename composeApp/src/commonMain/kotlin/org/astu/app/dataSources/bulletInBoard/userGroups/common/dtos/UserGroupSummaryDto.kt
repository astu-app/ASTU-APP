package org.astu.app.dataSources.bulletInBoard.userGroups.common.dtos

import kotlinx.serialization.Serializable

/**
 * Краткая информация о группе пользователей
 */
@Serializable
data class UserGroupSummaryDto(val id: String, val name: String)