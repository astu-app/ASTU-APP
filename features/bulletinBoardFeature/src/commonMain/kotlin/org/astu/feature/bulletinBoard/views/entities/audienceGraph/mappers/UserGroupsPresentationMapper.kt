package org.astu.feature.bulletinBoard.views.entities.audienceGraph.mappers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.entities.audience.User
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroup
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.INode
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.Leaf
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.Node
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary
import kotlin.jvm.JvmName

object UserGroupsPresentationMapper {
    private val mappedNodes: MutableMap<Uuid, INode> = mutableMapOf()


    @JvmName("AudienceHierarchyToDetailedUserGroupHierarchy")
    fun UserGroupHierarchy.toDetailedUserGroupHierarchy(): List<INode> {
        mappedNodes.clear()
        return this.roots.map { mapDetailedUserGroupHierarchyNode(it) }
    }

    @JvmName("AudienceHierarchyToShortUserGroupHierarchy")
    fun UserGroupHierarchy.toShortUserGroupHierarchy(onUserGroupClicked: (UserGroup) -> Unit = { }): List<INode> {
        mappedNodes.clear()
        return this.roots.map { mapShortUserGroupHierarchyNode(it, onUserGroupClicked) }
    }



    private fun mapDetailedUserGroupHierarchyNode(userGroup: UserGroup): Node {
        if (mappedNodes.containsKey(userGroup.id)) {
            return mappedNodes[userGroup.id] as Node
        }

        val members = mapMembers(userGroup.members)
        val childUserGroups = userGroup.userGroups.map { mapDetailedUserGroupHierarchyNode(it) }

        val childrenNodes = members + childUserGroups
        val userGroupNode = Node(
            children = childrenNodes,
            content = makeStaticUserGroupText(userGroup.name)
        )

        userGroupNode.content = makeStaticUserGroupText(userGroup.name)
        childrenNodes.forEach { it.parentNodes.add(userGroupNode) }

        mappedNodes[userGroup.id] = userGroupNode
        return userGroupNode
    }

    private fun mapShortUserGroupHierarchyNode(userGroup: UserGroup, onUserGroupClicked: (UserGroup) -> Unit): Node {
        if (mappedNodes.containsKey(userGroup.id)) {
            return mappedNodes[userGroup.id] as Node
        }

        val childUserGroups = userGroup.userGroups.map { mapShortUserGroupHierarchyNode(it, onUserGroupClicked) }
        val userGroupNode = Node(
            children = childUserGroups,
            content = makeStaticUserGroupText(userGroup.name) {
                onUserGroupClicked.invoke(userGroup)
            }
        )

        childUserGroups.forEach { it.parentNodes.add(userGroupNode) }

        mappedNodes[userGroup.id] = userGroupNode
        return userGroupNode
    }

    private fun makeStaticUserGroupText(text: String, onClick: () -> Unit = { }): @Composable () -> Unit = {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(onClick = onClick),
        )
    }


    private fun mapMembers(members: List<User>): List<Leaf> {
        return members.map { mapMember(it) }
    }

    private fun mapMember(member: User): Leaf {
        if (mappedNodes.containsKey(member.id)) {
            return mappedNodes[member.id] as Leaf
        }

        val userSummary = UserSummary(member.id, member.firstName, member.secondName, member.patronymic)
        val userLeaf = Leaf(content = { })

        userLeaf.content = makeStaticUserText(userSummary)

        mappedNodes[member.id] = userLeaf
        return userLeaf
    }

    private fun makeStaticUserText(
        user: UserSummary,
        modifier: Modifier = Modifier,
    ): @Composable () -> Unit = {
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
    }
}