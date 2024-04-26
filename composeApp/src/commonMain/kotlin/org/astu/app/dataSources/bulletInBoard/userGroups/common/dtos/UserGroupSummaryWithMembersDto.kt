package org.astu.app.dataSources.bulletInBoard.userGroups.common.dtos

import kotlinx.serialization.Serializable
import org.astu.app.dataSources.bulletInBoard.users.common.dtos.UserSummaryDto

/**
 * Краткая информация о группе пользователей, включая ее участников
 */
@Serializable
data class UserGroupSummaryWithMembersDto(val summary: UserGroupSummaryDto, val members: List<UserSummaryDto>)