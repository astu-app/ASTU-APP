package org.astu.feature.bulletinBoard.models.entities.audience

import com.benasher44.uuid.Uuid

/**
 * Данные для редактирования группы пользователей
 * @param id Идентификатор группы пользователей
 * @param name Название группы пользователей
 * @param admin Администратор группы пользователей
 * @param members Краткая информация об участниках группы пользователей, включая их права
 * @param potentialMembers Список пользователей, которые могут быть назначены администратором или добавлены в группу пользователей. В списке отсутствуют текущий администратор и участники группы пользователей
 */
data class ContentForUserGroupEditing (
    val id: Uuid,
    val name: String,
    val admin: User?,
    val members: List<MemberWithRights>,
    val potentialMembers: List<User>,
//    val parents: List<UserGroupSummary>, // remove
//    val children: List<UserGroupSummary>,
//    val potentialRelatives: List<UserGroupSummary>,
)