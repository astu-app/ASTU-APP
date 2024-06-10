package org.astu.feature.bulletinBoard.models.entities.audience

import com.benasher44.uuid.Uuid

data class UserGroupSummary(
    val id: Uuid,
    val name: String,
    val adminName: String?,
)