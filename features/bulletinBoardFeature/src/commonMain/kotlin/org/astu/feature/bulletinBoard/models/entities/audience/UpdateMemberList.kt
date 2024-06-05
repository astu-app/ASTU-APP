package org.astu.feature.bulletinBoard.models.entities.audience

import com.benasher44.uuid.Uuid

data class UpdateMemberList(
    val idsToRemove: List<Uuid>,
    val newMembers: List<MemberIdWithRights>
)
