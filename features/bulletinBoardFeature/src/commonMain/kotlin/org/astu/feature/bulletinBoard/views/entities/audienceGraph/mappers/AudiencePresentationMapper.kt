package org.astu.feature.bulletinBoard.views.entities.audienceGraph.mappers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.entities.audience.AudienceHierarchy
import org.astu.feature.bulletinBoard.models.entities.audience.User
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroup
import org.astu.feature.bulletinBoard.views.components.announcements.common.SelectableUserSummary
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.INode
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.ISelectableNode
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.SelectableLeaf
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.SelectableNode
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary
import org.astu.infrastructure.components.CheckboxRow

/**
 * Класс, преобразующий иерархию аудитории объявления в формат представления
 * @param audienceHierarchy аудитория объявления в иерархичной форме
 * @param selectedMemberIds массив с идентификаторами пользователей, в который идентификаторы добавляются или удаляются при нажатии соответствующих графических элементов
 */
class AudiencePresentationMapper(
    private val audienceHierarchy: AudienceHierarchy,
    private val selectedMemberIds: MutableSet<Uuid>
) {
    private val mappedNodes: MutableMap<Uuid, ISelectableNode> = mutableMapOf()


    fun mapAudienceHierarchy(): List<INode> {
        return audienceHierarchy.roots.map { mapAudienceHierarchyNode(it) }
    }


    private fun mapAudienceHierarchyNode(userGroup: UserGroup): SelectableNode {
        if (mappedNodes.containsKey(userGroup.id)) {
            return mappedNodes[userGroup.id] as SelectableNode
        }

        val selectableMembers = mapMembers(userGroup.members)
        val selectableChildUserGroups = userGroup.userGroups.map { mapAudienceHierarchyNode(it) }

        val childrenNodes = selectableMembers + selectableChildUserGroups
        val selectableUserGroup = SelectableNode(children = childrenNodes, isSelected = mutableStateOf(childrenNodes.all { it.isSelected.value }), content = { })

        val memberIds = getUserGroupHierarchyMemberIds(userGroup)
        selectableUserGroup.content = makeSelectableUserGroupText(userGroup.name, selectableUserGroup) { newState ->
            selectableUserGroup.setSelectionState(newState)
            selectableUserGroup.receiveNotificationChildSelectionChanges()
            selectableUserGroup.parentNodes.forEach { it.receiveNotificationChildSelectionChanges() }

            if (newState) {
                selectedMemberIds.addAll(memberIds)
            } else {
                selectedMemberIds.removeAll(memberIds)
            }
        }

        childrenNodes.forEach { it.parentNodes.add(selectableUserGroup) }

        mappedNodes[userGroup.id] = selectableUserGroup
        return selectableUserGroup
    }

    private fun makeSelectableUserGroupText(
        text: String,
        userGroup: ISelectableNode,
        onCheckedStateChanged: (Boolean) -> Unit
    ): @Composable () -> Unit {
        // Нельзя использовать userGroup.content вместо text, так как в момент вызова метода userGroup.content еще не
        // инициализирован
        return {
            CheckboxRow(
                title = {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    )
                },
                state = userGroup.isSelected,
                onCheckedStateChange = onCheckedStateChanged
            )
        }
    }

    private fun mapMembers(members: List<User>): List<SelectableLeaf> {
        return members.map { mapMember(it) }
    }

    private fun mapMember(member: User): SelectableLeaf {
        if (mappedNodes.containsKey(member.id)) {
            return mappedNodes[member.id] as SelectableLeaf
        }

        val userSummary = UserSummary(member.id, member.firstName, member.secondName, member.patronymic)
        val selectableUserSummary = SelectableUserSummary(userSummary = userSummary, isSelected = mutableStateOf(selectedMemberIds.contains(member.id)))
        val selectableUser = SelectableLeaf(isSelected = selectableUserSummary.isSelected, content = { })

        selectableUser.content = makeSelectableUserText(selectableUserSummary) { newState ->
            selectableUser.setSelectionState(newState)
            selectableUser.parentNodes.forEach { it.receiveNotificationChildSelectionChanges() }

            if (newState) {
                selectedMemberIds.add(member.id)
            } else {
                selectedMemberIds.remove(member.id)
            }
        }


        mappedNodes[member.id] = selectableUser
        return selectableUser
    }

    private fun makeSelectableUserText(
        user: SelectableUserSummary,
        modifier: Modifier = Modifier,
        onCheckedStateChanged: (Boolean) -> Unit
    ): @Composable () -> Unit {
        return {
            CheckboxRow(
                title = {
                    Column(modifier = modifier) {
                        Text(
                            text = user.firstName
                        )
                        val secondPartOfName =
                            if (user.patronymic != null)
                                "${user.secondName} ${user.patronymic}"
                            else user.secondName
                        Text(
                            text = secondPartOfName,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                },
                state = user.isSelected,
                onCheckedStateChange = onCheckedStateChanged
            )
        }
    }

    private fun getUserGroupHierarchyMemberIds(userGroup: UserGroup): Set<Uuid> {
        val ids = mutableSetOf<Uuid>()
        ids.addAll(userGroup.members.map { it.id })

        userGroup.userGroups.forEach {
            ids.addAll(getUserGroupHierarchyMemberIds(it))
        }

        return ids
    }
}