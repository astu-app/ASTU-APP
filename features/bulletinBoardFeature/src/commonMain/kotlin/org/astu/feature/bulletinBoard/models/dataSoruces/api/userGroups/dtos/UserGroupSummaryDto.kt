package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos

import kotlinx.serialization.Serializable

/**
 * Краткая информация о группе пользователей
 */
@Serializable
data class UserGroupSummaryDto(val id: String, val name: String, val adminName: String?)