package org.astu.feature.bulletinBoard.views.entities.userGroups.details

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.entities.userGroups.summary.UserGroupSummaryContent
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary

data class UserGroupDetailsContent (
    val id: Uuid,
    val name: String,
    val admin: UserSummary?,
    val members: Map<Uuid, UserSummaryWithUserRights>,
    val parents: List<UserGroupSummaryContent>,
    val children: List<UserGroupSummaryContent>,
)