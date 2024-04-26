package org.astu.app.dataSources.bulletInBoard.userGroups.announcementAudience

import com.benasher44.uuid.uuidFrom
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.app.GlobalDIContext
import org.astu.app.dataSources.bulletInBoard.common.readUnsuccessCode
import org.astu.app.dataSources.bulletInBoard.common.responses.ContentWithError
import org.astu.app.dataSources.bulletInBoard.userGroups.common.dtos.UserGroupHierarchyDto
import org.astu.app.dataSources.bulletInBoard.userGroups.common.dtos.UserGroupHierarchyNodeDto
import org.astu.app.dataSources.bulletInBoard.userGroups.common.dtos.UserGroupSummaryWithMembersDto
import org.astu.app.dataSources.bulletInBoard.userGroups.common.responses.GetUserHierarchyResponses
import org.astu.app.dataSources.bulletInBoard.users.common.dtos.UserSummaryDto
import org.astu.app.models.bulletInBoard.entities.audience.AudienceHierarchy
import org.astu.app.models.bulletInBoard.entities.audience.User
import org.astu.app.models.bulletInBoard.entities.audience.UserGroup
import org.kodein.di.instance

class ApiAnnouncementAudienceDataSource : AnnouncementAudienceDataSource {
    private val client: HttpClient by GlobalDIContext.instance()


    override suspend fun getAudienceForCreation(): ContentWithError<AudienceHierarchy, GetUserHierarchyResponses> {
        val response = client.get("api/usergroups/get-user-hierarchy")

        if (!response.status.isSuccess())
            return ContentWithError(null, error = readUnsuccessCode<GetUserHierarchyResponses>(response))

        val hierarchyDto = response.body<UserGroupHierarchyDto>()
        val hierarchy = mapHierarchy(hierarchyDto)
        return ContentWithError(hierarchy, error = null)
    }



    private fun mapHierarchy(dto: UserGroupHierarchyDto): AudienceHierarchy {
        val userGroupPlainMap = mapUserGroupsToPlainMap(dto.userGroups)
        val roots = dto.roots.map { mapUserGroup(it, userGroupPlainMap) }
        return AudienceHierarchy(roots)
    }

    private fun mapUserGroup(nodeDto: UserGroupHierarchyNodeDto, userGroupPlainMap: Map<String, UserGroup>): UserGroup {
        // предполагается, что в словаре будут присутствовать все рассматриваемые группы пользователей
        val userGroup = userGroupPlainMap[nodeDto.id]!!
        nodeDto.children.forEach {
            if (userGroup.userGroups.any { ug -> ug.id.toString() == it.id })
                return@forEach

            val child = mapUserGroup(it, userGroupPlainMap)
            userGroup.userGroups.add(child)
        }

        return userGroup
    }

    private fun mapUserGroupsToPlainMap(userGroups: List<UserGroupSummaryWithMembersDto>): Map<String, UserGroup> {
        return userGroups.associateBy( {it.summary.id}, {mapUserGroup(it)} )
    }

    private fun mapUserGroup(dto: UserGroupSummaryWithMembersDto): UserGroup {
        return UserGroup(uuidFrom(dto.summary.id), dto.summary.name, mutableListOf(), mapMembers(dto.members))
    }

    private fun mapMembers(dtos: List<UserSummaryDto>): List<User> {
        return dtos.map { User(uuidFrom(it.id), it.firstName, it.secondName, it.patronymic) }
    }
}