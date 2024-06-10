package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos

import kotlinx.serialization.Serializable
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.dtos.UserSummaryDto

/**
 * @param id Идентификатор группы пользователей
 * @param name Название группы пользователей
 * @param admin Информация об администраторе группы
 * @param members Краткая информация об участниках группы пользователей
 * @param parents Родительские группы пользователей
 * @param children Дочерние группы пользователей
 */
@Serializable
data class UserGroupDetailsDto(
    val id: String,
    val name: String,
    val admin: UserSummaryDto?,
    val members: List<UserSummaryWithMemberRightsDto>,
    val parents: List<UserGroupSummaryDto>,
    val children: List<UserGroupSummaryDto>
)
