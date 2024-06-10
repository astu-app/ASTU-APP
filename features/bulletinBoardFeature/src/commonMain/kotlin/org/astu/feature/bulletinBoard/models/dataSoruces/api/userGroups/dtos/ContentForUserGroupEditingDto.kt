package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos

import kotlinx.serialization.Serializable
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.dtos.UserSummaryDto

/**
 * Данные для редактирования группы пользователей
 * @param id Идентификатор группы пользователей
 * @param name Название группы пользователей
 * @param admin Администратор группы пользователей
 * @param members Краткая информация об участниках группы пользователей, включая их права
 * @param users Список пользователей, которые могут быть назначены администратором или добавлены в группу пользователей. В списке отсутствуют текущий администратор и участники группы пользователей
 */
@Serializable
data class ContentForUserGroupEditingDto(
    val id: String,
    val name: String,
    val admin: UserSummaryDto?,
    val members: List<UserSummaryWithMemberRightsDto>,
    val users: List<UserSummaryDto>,
//    val parents: List<UserGroupSummaryDto>, // remove
//    val children: List<UserGroupSummaryDto>,
//    val usergroups: List<UserGroupSummaryDto>,
)
