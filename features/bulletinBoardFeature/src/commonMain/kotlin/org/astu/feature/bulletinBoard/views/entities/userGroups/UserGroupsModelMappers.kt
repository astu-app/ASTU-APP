package org.astu.feature.bulletinBoard.views.entities.userGroups

import org.astu.feature.bulletinBoard.models.entities.audience.CreateUserGroup
import org.astu.feature.bulletinBoard.models.entities.audience.MemberIdWithRights
import org.astu.feature.bulletinBoard.models.entities.audience.MemberRights
import org.astu.feature.bulletinBoard.views.entities.userGroups.common.AddUserGroupMember
import org.astu.feature.bulletinBoard.views.entities.userGroups.creation.CreateUserGroupContent
import kotlin.jvm.JvmName

object UserGroupsModelMappers {
    @JvmName("CreateUserGroupContentToModel")
    fun CreateUserGroupContent.toModel(): CreateUserGroup =
        CreateUserGroup(
            name = this.name.value,
            adminId = this.admin.value?.id,
            members = this.addedMembers.values.toModels(),
            childUserGroupIds = this.selectedChildUserGroupIds,
            parentUserGroupIds = this.selectedParentUserGroupIds
        )

    @JvmName("AddUserGroupMemberCollectionToModels")
    fun Collection<AddUserGroupMember>.toModels(): List<MemberIdWithRights> =
        this.map { it.toModel() }

    @JvmName("AddUserGroupMemberToModel")
    fun AddUserGroupMember.toModel(): MemberIdWithRights =
        MemberIdWithRights(
            userId = this.userId,
            usergroupId = null,
            rights  = MemberRights(
                canViewAnnouncements = this.canViewAnnouncements.value,
                canCreateAnnouncements = this.canCreateAnnouncements.value,
                canCreateSurveys = this.canCreateSurveys.value,
                canRuleUserGroupHierarchy = canRuleUserGroupHierarchy.value,
                canViewUserGroupDetails = this.canViewUserGroupDetails.value,
                canCreateUserGroups = this.canCreateUserGroups.value,
                canEditUserGroups = this.canEditUserGroups.value,
                canEditMembers = this.canEditMembers.value,
                canEditMemberRights = this.canEditMemberRights.value,
                canEditUserGroupAdmin = this.canEditUserGroupAdmin.value,
                canDeleteUserGroup = this.canDeleteUserGroup.value,
            ),
        )
}