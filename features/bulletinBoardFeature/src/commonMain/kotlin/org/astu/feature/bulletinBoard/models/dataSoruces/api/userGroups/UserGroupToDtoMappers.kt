package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups

import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos.*
import org.astu.feature.bulletinBoard.models.entities.audience.*
import kotlin.jvm.JvmName

@Suppress("unused")
object UserGroupToDtoMappers {
    @JvmName("CreateUserGroupToDto")
    fun CreateUserGroup.toDto(): CreateUserGroupDto =
        CreateUserGroupDto(
            name = this.name,
            adminId = this.adminId?.toString(),
            members = this.members.toDtos(),
            parentIds = parentUserGroupIds.map { it.toString() },
            childIds = childUserGroupIds.map { it.toString() }
        )

    @JvmName("MemberIdWithRightsCollectionToDtos")
    fun Collection<MemberIdWithRights>.toDtos(): List<UserIdWithMemberRightsDto> =
        this.map { it.toDto() }

    @JvmName("MemberIdWithRightsToDto")
    fun MemberIdWithRights.toDto(): UserIdWithMemberRightsDto =
        UserIdWithMemberRightsDto(
            userId = this.userId.toString(),
            usergroupId = this.usergroupId?.toString(),
            rights = rights.toDto(),

            )

    @JvmName("MemberRightsToDto")
    fun MemberRights.toDto(): MemberRightsDto =
        MemberRightsDto(
            canCreateAnnouncements = this.canCreateAnnouncements,
            canCreateSurveys = this.canCreateSurveys,
            canRuleUserGroupHierarchy = this.canRuleUserGroupHierarchy,
            canViewUserGroupDetails = this.canViewUserGroupDetails,
            canCreateUserGroups = this.canCreateUserGroups,
            canEditUserGroups = this.canEditUserGroups,
            canEditMembers = this.canEditMembers,
            canEditMemberRights = this.canEditMemberRights,
            canEditUserGroupAdmin = this.canEditUserGroupAdmin,
            canDeleteUserGroup = this.canDeleteUserGroup,
        )

    @JvmName("UpdateUserGroupToDto")
    fun UpdateUserGroup.toDto(): UpdateUserGroupDto =
        UpdateUserGroupDto(
            id = this.id.toString(),
            name = this.name,
            adminChanged = this.adminChanged,
            adminId = this.adminId?.toString(),
            members = this.members.toDto(),
//            childGroups = this.childGroups.toDto(), // remove
//            parentGroups = this.parentGroups.toDto(),
        )

    @JvmName("UpdateMemberListToDto")
    fun UpdateMemberList.toDto(): UpdateMemberListDto =
        UpdateMemberListDto(
            idsToRemove = this.idsToRemove.map { it.toString() },
            newMembers = this.newMembers.toDtos(),
        )
}