package org.astu.feature.bulletinBoard.models.entities.audience

import com.benasher44.uuid.Uuid

data class UserGroupDetails (
    val id: Uuid,
    val name: String,
    val admin: User?,
    val members: List<MemberWithRights>,
    val parents: List<UserGroupSummary>,
    val children: List<UserGroupSummary>
)