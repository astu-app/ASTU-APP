package org.astu.feature.bulletinBoard.models.entities.audience

import com.benasher44.uuid.Uuid

data class MemberIdWithRights(
    val userId: Uuid,
    val usergroupId: Uuid?,
    val rights: MemberRights
)
