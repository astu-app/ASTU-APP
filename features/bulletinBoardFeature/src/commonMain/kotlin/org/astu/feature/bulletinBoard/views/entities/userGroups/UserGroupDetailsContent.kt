package org.astu.feature.bulletinBoard.views.entities.userGroups

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary

data class UserGroupDetailsContent (
    val id: Uuid,
    val name: String,
    val admin: UserSummary,
    val members: List<UserSummary>,
    val parents: List<UserGroupSummaryContent>,
    val children: List<UserGroupSummaryContent>,
)