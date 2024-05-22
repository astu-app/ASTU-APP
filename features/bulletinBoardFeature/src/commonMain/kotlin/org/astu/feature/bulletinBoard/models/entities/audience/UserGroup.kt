package org.astu.feature.bulletinBoard.models.entities.audience

import com.benasher44.uuid.Uuid

data class UserGroup(
    val id: Uuid,
    val name: String,
    val adminName: String?,
    val userGroups: MutableList<UserGroup>,
    val members: List<User>
)