package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos

import kotlinx.serialization.Serializable

@Serializable
data class MemberRightsDto (
    val canViewAnnouncements: Boolean,
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