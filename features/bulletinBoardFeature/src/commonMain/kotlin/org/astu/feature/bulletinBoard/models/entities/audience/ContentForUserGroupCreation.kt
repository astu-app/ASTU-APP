package org.astu.feature.bulletinBoard.models.entities.audience

data class ContentForUserGroupCreation(
    val users: List<User>,
    val userGroups: List<UserGroupSummary>
)
