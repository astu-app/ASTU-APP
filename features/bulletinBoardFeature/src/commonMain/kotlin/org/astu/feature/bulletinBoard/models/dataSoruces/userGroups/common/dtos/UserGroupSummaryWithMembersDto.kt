package org.astu.feature.bulletinBoard.models.dataSoruces.userGroups.common.dtos

import kotlinx.serialization.Serializable
import org.astu.feature.bulletinBoard.models.dataSoruces.dtos.UserSummaryDto

/**
 * Краткая информация о группе пользователей, включая ее участников
 */
@Serializable
data class UserGroupSummaryWithMembersDto(val summary: UserGroupSummaryDto, val members: List<UserSummaryDto>)