package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos.*
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.UserMappers.toModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.UserMappers.toModels
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroup
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupDetails
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupSummary
import kotlin.jvm.JvmName

object UserGroupMappers {
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
        UserGroup(uuidFrom(this.summary.id), this.summary.name, mutableListOf(), this.members.toModels())

    @JvmName("UserGroupSummaryWithMembersDtoCollectionToModels")
    fun Collection<UserGroupSummaryWithMembersDto>.toModels(): List<UserGroup> =
        this.map { it.toModel() }

    @JvmName("UserGroupDetailsDtoToModel")
    fun UserGroupDetailsDto.toModel(): UserGroupDetails =
        UserGroupDetails(
            id = uuidFrom(this.id),
            name = this.name,
            admin = this.admin.toModel(),
            members = this.members.toModels(),
            parents = this.parents.toModels(),
            children = this.children.toModels(),
        )

    @JvmName("UserGroupSummaryDtoCollectionToModel")
    fun Collection<UserGroupSummaryDto>.toModels(): List<UserGroupSummary> =
        this.map { it.toModel() }

    @JvmName("UserGroupDtoToModel")
    fun UserGroupSummaryDto.toModel(): UserGroupSummary =
        UserGroupSummary(id = uuidFrom(this.id), name = this.name)



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