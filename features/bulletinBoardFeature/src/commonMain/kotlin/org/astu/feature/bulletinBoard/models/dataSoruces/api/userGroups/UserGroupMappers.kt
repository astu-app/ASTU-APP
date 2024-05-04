package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos.UserGroupHierarchyDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos.UserGroupHierarchyNodeDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos.UserGroupSummaryWithMembersDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.UserMappers.toModels
import org.astu.feature.bulletinBoard.models.entities.audience.AudienceHierarchy
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroup
import kotlin.jvm.JvmName

object UserGroupMappers {
    @JvmName("UserGroupHierarchyDtoToModel")
    fun UserGroupHierarchyDto.toModel(): AudienceHierarchy {
        var mappedUserGroups = this.userGroups
            .toModels()
            .associateBy { it.id.toString() }

        val mappedRoots = roots.map { mapAudienceHierarchy(it, null, mappedUserGroups) }
        return AudienceHierarchy(mappedRoots)
    }

    @JvmName("UserGroupSummaryWithMembersDtoToModel")
    fun UserGroupSummaryWithMembersDto.toModel(): UserGroup =
        UserGroup(uuidFrom(this.summary.id), this.summary.name, mutableListOf(), this.members.toModels())

    @JvmName("UserGroupSummaryWithMembersDtoCollectionToModels")
    fun Collection<UserGroupSummaryWithMembersDto>.toModels(): List<UserGroup> =
        this.map { it.toModel() }



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