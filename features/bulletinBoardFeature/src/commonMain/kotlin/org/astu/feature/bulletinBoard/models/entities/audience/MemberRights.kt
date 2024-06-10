package org.astu.feature.bulletinBoard.models.entities.audience

data class MemberRights(
    val canViewAnnouncements: Boolean,
    val canCreateAnnouncements: Boolean,
    val canCreateSurveys: Boolean,
    val canViewUserGroupDetails: Boolean,
    val canCreateUserGroups: Boolean,
    val canEditUserGroups: Boolean,
    val canEditMembers: Boolean,
    val canEditMemberRights: Boolean,
    val canEditUserGroupAdmin: Boolean,
    val canDeleteUserGroup: Boolean,
)
