package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos

import kotlinx.serialization.Serializable
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.dtos.CheckableUserSummaryDto

/**
 * Краткая информация о группе пользователей, включая ее участников
 */
@Serializable
data class UserGroupSummaryWithMembersDto(val summary: UserGroupSummaryDto, val members: List<CheckableUserSummaryDto>)