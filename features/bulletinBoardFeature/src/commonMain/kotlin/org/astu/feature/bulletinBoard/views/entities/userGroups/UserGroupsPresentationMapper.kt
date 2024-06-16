package org.astu.feature.bulletinBoard.views.entities.userGroups

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroup
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupDetails
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupSummary
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.INode
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.Node
import org.astu.feature.bulletinBoard.views.entities.userGroups.details.UserGroupDetailsContent
import org.astu.feature.bulletinBoard.views.entities.userGroups.summary.UserGroupSummaryContent
import org.astu.feature.bulletinBoard.views.entities.users.UserToPresentationMappers.toPresentation
import kotlin.jvm.JvmName

object UserGroupsPresentationMapper {
    private val mappedNodes: MutableMap<Uuid, INode> = mutableMapOf()


    @JvmName("AudienceHierarchyToShortUserGroupHierarchy")
    fun UserGroupHierarchy.toShortUserGroupHierarchy(
        onUserGroupClick: (UserGroup) -> Unit = { },
        onUserGroupLongPress: (UserGroup, LayoutCoordinates, DpOffset) -> Unit,
    ): List<INode> {
        mappedNodes.clear()
        return this.roots.map { mapShortUserGroupHierarchyNode(it, onUserGroupClick, onUserGroupLongPress) }
    }

    @JvmName("UserGroupDetailsToPresentation")
    fun UserGroupDetails.toPresentation(): UserGroupDetailsContent =
        UserGroupDetailsContent(
            id = this.id,
            name = this.name,
            admin = this.admin?.toPresentation(),
            members = this.members.associateBy({ it.user.id }, { it.toPresentation() }),
            parents = this.parents.toPresentations(),
            children = this.children.toPresentations()
        )

    @JvmName("UserGroupSummaryCollectionToPresentations")
    fun Collection<UserGroupSummary>.toPresentations(): List<UserGroupSummaryContent> =
        this.map { it.toPresentation() }

    @JvmName("UserGroupSummaryToPresentation")
    fun UserGroupSummary.toPresentation(): UserGroupSummaryContent =
        UserGroupSummaryContent(this.id, this.name, this.adminName)



    private fun mapShortUserGroupHierarchyNode(
        userGroup: UserGroup,
        onUserGroupClicked: (UserGroup) -> Unit,
        onUserGroupLongPress: (UserGroup, LayoutCoordinates, DpOffset) -> Unit,
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
                onLongPress = { coordinates, offset -> onUserGroupLongPress.invoke(userGroup, coordinates, offset) },
            )
        )

        childUserGroups.forEach { it.parentNodes.add(userGroupNode) }

        mappedNodes[userGroup.id] = userGroupNode
        return userGroupNode
    }

    private fun makeStaticUserGroupText(
        groupName: String,
        adminName: String?,
        onTap: (Offset) -> Unit,
        onLongPress: (LayoutCoordinates, DpOffset) -> Unit,
    ): @Composable () -> Unit = {
        var globalPosition: LayoutCoordinates? = remember { null }
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = onTap,
                        onLongPress = {
                            val offset = DpOffset(it.x.toDp(), it.y.toDp())
                            onLongPress.invoke(globalPosition!!, offset)
                        }
                    )
                }
                .onGloballyPositioned { globalPosition = it },
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
}