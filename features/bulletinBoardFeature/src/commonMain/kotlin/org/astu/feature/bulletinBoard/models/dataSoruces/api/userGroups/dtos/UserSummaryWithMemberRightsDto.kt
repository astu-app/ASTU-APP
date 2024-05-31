package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos

import kotlinx.serialization.Serializable
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.dtos.UserSummaryDto

/**
 * Идентификатор пользователя с правами в группе пользователей
 */
@Serializable
data class UserSummaryWithMemberRightsDto (
    val user: UserSummaryDto,
    val rights: MemberRightsDto
)