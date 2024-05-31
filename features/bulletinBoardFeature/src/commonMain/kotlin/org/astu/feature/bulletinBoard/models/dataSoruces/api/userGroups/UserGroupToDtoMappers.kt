package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.UserGroupToModelMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos.MemberRightsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos.UserGroupDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos.UserSummaryWithMemberRightsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.UserMappers.toModel
import org.astu.feature.bulletinBoard.models.entities.audience.MemberRights
import org.astu.feature.bulletinBoard.models.entities.audience.MemberWithRights
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupDetails
import kotlin.jvm.JvmName

object UserGroupToDtoMappers {
    @JvmName("UserGroupDetailsDtoToModel")
    fun UserGroupDetailsDto.toModel(): UserGroupDetails =
        UserGroupDetails(
            id = uuidFrom(this.id),
            name = this.name,
            admin = this.admin?.toModel(),
            members = this.members.toModels(),
            parents = this.parents.toModels(),
            children = this.children.toModels(),
        )

    @JvmName("UserSummaryWithMemberRightsDtoCollectionToModels")
    fun Collection<UserSummaryWithMemberRightsDto>.toModels(): List<MemberWithRights> =
        this.map { it.toModel() }

    @JvmName("UserSummaryWithMemberRightsDtoToModel")
    fun UserSummaryWithMemberRightsDto.toModel(): MemberWithRights =
        MemberWithRights(
            user = this.user.toModel(),
            rights = this.rights.toModel()
        )

    @JvmName("MemberRightsDtoToModel")
    fun MemberRightsDto.toModel(): MemberRights =
        MemberRights(
            canViewAnnouncements = this.canViewAnnouncements,
            canCreateAnnouncements = this.canCreateAnnouncements,
            canCreateSurveys = this.canCreateSurveys,
            canViewUserGroupDetails = this.canViewUserGroupDetails,
            canCreateUserGroups = this.canCreateUserGroups,
            canEditUserGroups = this.canEditUserGroups,
            canEditMembers = this.canEditMembers,
            canEditMemberRights = this.canEditMemberRights,
            canEditUserGroupAdmin = this.canEditUserGroupAdmin,
            canDeleteUserGroup = this.canDeleteUserGroup,
        )
}