package org.astu.app.infrastructure.mappers

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.benasher44.uuid.Uuid
import org.astu.app.components.CheckboxRow
import org.astu.app.components.bulletinBoard.announcements.common.SelectableUserSummary
import org.astu.app.components.bulletinBoard.common.models.UserSummary
import org.astu.app.entities.bulletInBoard.audienceGraph.INode
import org.astu.app.entities.bulletInBoard.audienceGraph.ISelectableNode
import org.astu.app.entities.bulletInBoard.audienceGraph.SelectableLeaf
import org.astu.app.entities.bulletInBoard.audienceGraph.SelectableNode
import org.astu.app.models.bulletInBoard.entities.audience.AudienceHierarchy
import org.astu.app.models.bulletInBoard.entities.audience.User
import org.astu.app.models.bulletInBoard.entities.audience.UserGroup

class AudienceMapper(
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

        val selectableNodes = selectableMembers + selectableChildUserGroups
        val selectableUserGroup = SelectableNode(nodes = selectableNodes, content = { })
        selectableUserGroup.content = makeSelectableUserGroupText(userGroup.name, selectableUserGroup) { newState ->
            selectableUserGroup.setSelectionState(newState)
            if (newState) {
                selectedMemberIds.addAll(getUserGroupHierarchyMemberIds(userGroup))
            } else {
                selectedMemberIds.removeAll(getUserGroupHierarchyMemberIds(userGroup))
            }
        }

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
        val selectableUserSummary = SelectableUserSummary(userSummary)
        val leafContent = makeSelectableUserText(selectableUserSummary) { newState ->
            selectableUserSummary.isSelected.value = newState
            if (newState) {
                selectedMemberIds.add(member.id)
            } else {
                selectedMemberIds.remove(member.id)
            }
        }

        val selectableUser = SelectableLeaf(leafContent, selectableUserSummary.isSelected)

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