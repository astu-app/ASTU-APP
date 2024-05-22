package org.astu.feature.bulletinBoard.views.entities.userGroups;

import com.benasher44.uuid.Uuid

data class UserGroupSummaryContent (
    val id: Uuid,
    val name: String,
    val adminName: String?,
)
