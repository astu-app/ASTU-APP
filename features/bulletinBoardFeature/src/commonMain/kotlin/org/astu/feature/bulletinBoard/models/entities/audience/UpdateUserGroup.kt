package org.astu.feature.bulletinBoard.models.entities.audience

import com.benasher44.uuid.Uuid

data class UpdateUserGroup(
    val id: Uuid,
    val name: String?,
    val adminChanged: Boolean,
    val adminId: Uuid?,
    val members: UpdateMemberList,
//    val childGroups: UpdateIdentifierList, // remove
//    val parentGroups: UpdateIdentifierList,
)
