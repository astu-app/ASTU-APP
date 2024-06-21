package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos.*
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.UserMappers.toModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.UserMappers.toModels
import org.astu.feature.bulletinBoard.models.entities.audience.*
import kotlin.jvm.JvmName

@Suppress("unused")
object UserGroupToModelMappers {
    @JvmName("UserGroupHierarchyDtoToModel")
    fun UserGroupHierarchyDto.toModel(): UserGroupHierarchy {
        val mappedUserGroups = this.userGroups
            .toModels()
            .associateBy { it.id.toString() }

        val mappedRoots = roots.map { mapAudienceHierarchy(it, null, mappedUserGroups) }
        return UserGroupHierarchy(mappedRoots)
    }

    @JvmName("UserGroupSummaryWithMembersDtoToModel")
    fun UserGroupSummaryWithMembersDto.toModel(): UserGroup =
        UserGroup(uuidFrom(this.summary.id), this.summary.name, this.summary.adminName, mutableListOf(), this.members.toModels())

    @JvmName("UserGroupSummaryWithMembersDtoCollectionToModels")
    fun Collection<UserGroupSummaryWithMembersDto>.toModels(): List<UserGroup> =
        this.map { it.toModel() }

    @JvmName("UserGroupSummaryDtoCollectionToModel")
    fun Collection<UserGroupSummaryDto>.toModels(): List<UserGroupSummary> =
        this.map { it.toModel() }

    @JvmName("UserGroupSummaryDtoArrayToModel")
    fun Array<UserGroupSummaryDto>.toModels(): List<UserGroupSummary> =
        this.map { it.toModel() }

    @JvmName("UserGroupDtoToModel")
    fun UserGroupSummaryDto.toModel(): UserGroupSummary =
        UserGroupSummary(id = uuidFrom(this.id), name = this.name, adminName = this.adminName)

    @JvmName("GetUsergroupCreateContentDtoToModel")
    fun GetUsergroupCreateContentDto.toModel(): ContentForUserGroupCreation =
        ContentForUserGroupCreation(users = this.users.toModels(), userGroups = this.userGroups.toModels())

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
            canRuleUserGroupHierarchy = canRuleUserGroupHierarchy,
            canViewUserGroupDetails = this.canViewUserGroupDetails,
            canCreateUserGroups = this.canCreateUserGroups,
            canEditUserGroups = this.canEditUserGroups,
            canEditMembers = this.canEditMembers,
            canEditMemberRights = this.canEditMemberRights,
            canEditUserGroupAdmin = this.canEditUserGroupAdmin,
            canDeleteUserGroup = this.canDeleteUserGroup,
        )

    @JvmName("ContentForUserGroupEditingDtoToModel")
    fun ContentForUserGroupEditingDto.toModel(): ContentForUserGroupEditing =
        ContentForUserGroupEditing(
            id = uuidFrom(this.id),
            name = this.name,
            admin = this.admin?.toModel(),
            members = this.members.toModels(),
            potentialMembers = this.users.toModels(),
//            parents = this.parents.toModels(), // remove
//            children = this.children.toModels(),
//            potentialRelatives = this.usergroups.toModels(),
        )



    private fun mapAudienceHierarchy(
        current: UserGroupHierarchyNodeDto,
        previous: UserGroupHierarchyNodeDto?,
        mappedUserGroups: Map<String, UserGroup>
    ): UserGroup {
        val currentModel = mappedUserGroups[current.id]!!

        if (previous != null) {
            val previousModel = mappedUserGroups[previous.id]!!
            previousModel.userGroups.add(currentModel)
        }

        current.children.forEach { mapAudienceHierarchy(it, current, mappedUserGroups) }
        return currentModel
    }
}