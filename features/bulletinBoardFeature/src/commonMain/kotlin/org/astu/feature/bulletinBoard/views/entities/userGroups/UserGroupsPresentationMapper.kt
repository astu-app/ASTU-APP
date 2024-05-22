package org.astu.feature.bulletinBoard.views.entities.userGroups

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.entities.audience.*
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.INode
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.Leaf
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.Node
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary
import org.astu.feature.bulletinBoard.views.entities.users.UserToPresentationMappers.toPresentation
import org.astu.feature.bulletinBoard.views.entities.users.UserToPresentationMappers.toPresentations
import kotlin.jvm.JvmName

object UserGroupsPresentationMapper {
    private val mappedNodes: MutableMap<Uuid, INode> = mutableMapOf()


    @JvmName("AudienceHierarchyToDetailedUserGroupHierarchy")
    fun UserGroupHierarchy.toDetailedUserGroupHierarchy(): List<INode> {
        mappedNodes.clear()
        return this.roots.map { mapDetailedUserGroupHierarchyNode(it) }
    }

    @JvmName("AudienceHierarchyToShortUserGroupHierarchy")
    fun UserGroupHierarchy.toShortUserGroupHierarchy(
        onUserGroupClick: (UserGroup) -> Unit = { },
        onUserGroupLongPress: (UserGroup, DpOffset) -> Unit
    ): List<INode> {
        mappedNodes.clear()
        return this.roots.map { mapShortUserGroupHierarchyNode(it, onUserGroupClick, onUserGroupLongPress) }
    }

    @JvmName("UserGroupDetailsToPresentation")
    fun UserGroupDetails.toPresentation(): UserGroupDetailsContent =
        UserGroupDetailsContent(
            this.id,
            this.name,
            this.admin.toPresentation(),
            this.members.toPresentations(),
            this.parents.toPresentations(),
            this.children.toPresentations()
        )

    @JvmName("UserGroupSummaryCollectionToPresentations")
    fun Collection<UserGroupSummary>.toPresentations(): List<UserGroupSummaryContent> =
        this.map { it.toPresentation() }

    @JvmName("UserGroupSummaryToPresentation")
    fun UserGroupSummary.toPresentation(): UserGroupSummaryContent =
        UserGroupSummaryContent(this.id, this.name, this.adminName)


    private fun mapDetailedUserGroupHierarchyNode(userGroup: UserGroup): Node {
        if (mappedNodes.containsKey(userGroup.id)) {
            return mappedNodes[userGroup.id] as Node
        }

        val members = mapMembers(userGroup.members)
        val childUserGroups = userGroup.userGroups.map { mapDetailedUserGroupHierarchyNode(it) }

        val childrenNodes = members + childUserGroups
        val userGroupNode = Node(
            children = childrenNodes,
            content = makeStaticUserGroupText(userGroup.name, userGroup.adminName, { }, { })
        )

        userGroupNode.content = makeStaticUserGroupText(userGroup.name, userGroup.adminName, { }, { })
        childrenNodes.forEach { it.parentNodes.add(userGroupNode) }

        mappedNodes[userGroup.id] = userGroupNode
        return userGroupNode
    }

    private fun mapShortUserGroupHierarchyNode(
        userGroup: UserGroup,
        onUserGroupClicked: (UserGroup) -> Unit,
        onUserGroupLongPress: (UserGroup, DpOffset) -> Unit
    ): Node {
        if (mappedNodes.containsKey(userGroup.id)) {
            return mappedNodes[userGroup.id] as Node
        }

        val childUserGroups = userGroup.userGroups.map { mapShortUserGroupHierarchyNode(it, onUserGroupClicked, onUserGroupLongPress) }
        val userGroupNode = Node(
            children = childUserGroups,
            content = makeStaticUserGroupText(
                groupName = userGroup.name,
                adminName = userGroup.adminName,
                onTap = { onUserGroupClicked.invoke(userGroup) },
                onLongPress = { offset -> onUserGroupLongPress.invoke(userGroup, offset) }
            )
        )

        childUserGroups.forEach { it.parentNodes.add(userGroupNode) }

        mappedNodes[userGroup.id] = userGroupNode
        return userGroupNode
    }

    fun makeStaticUserGroupText(
        groupName: String,
        adminName: String?,
        onTap: (Offset) -> Unit,
        onLongPress: (DpOffset) -> Unit
    ): @Composable () -> Unit = {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = onTap,
                        onLongPress = {
                            val offset = DpOffset(it.x.toDp(), it.y.toDp()) // todo корректная позиция dropdown'а
                            onLongPress.invoke(offset)
                        }
                    )
                },
        ) {
            Text(
                text = groupName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )
            Text(
                text = adminName ?: "Без администратора",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )
        }

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