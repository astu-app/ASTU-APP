package org.astu.feature.bulletinBoard.models.dataSoruces.userGroups.common.dtos

import kotlinx.serialization.Serializable

/**
 * Краткая информация о группе пользователей
 */
@Serializable
data class UserGroupSummaryDto(val id: String, val name: String)