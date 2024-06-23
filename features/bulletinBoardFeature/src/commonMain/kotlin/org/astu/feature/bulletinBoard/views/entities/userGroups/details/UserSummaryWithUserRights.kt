package org.astu.feature.bulletinBoard.views.entities.userGroups.details

import org.astu.feature.bulletinBoard.views.entities.users.UserSummary

data class UserSummaryWithUserRights(
    val user: UserSummary,

    val canCreateAnnouncements: Boolean,
    val canCreateSurveys: Boolean,
    val canRuleUserGroupHierarchy: Boolean,
    val canViewUserGroupDetails: Boolean,
    val canCreateUserGroups: Boolean,
    val canEditUserGroups: Boolean,
    val canEditMembers: Boolean,
    val canEditMemberRights: Boolean,
    val canEditUserGroupAdmin: Boolean,
    val canDeleteUserGroup: Boolean,
)
