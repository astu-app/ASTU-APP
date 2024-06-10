package org.astu.feature.bulletinBoard.models.entities.audience

import com.benasher44.uuid.Uuid

data class CreateUserGroup (
    val name: String,
    val adminId: Uuid?,
    val members: List<MemberIdWithRights>,
    val childUserGroupIds: List<Uuid>,
    val parentUserGroupIds: List<Uuid>,
)