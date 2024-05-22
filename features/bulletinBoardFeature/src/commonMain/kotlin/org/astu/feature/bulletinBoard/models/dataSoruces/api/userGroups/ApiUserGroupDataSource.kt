package org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups

import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.astu.feature.bulletinBoard.models.dataSoruces.UserGroupDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.readUnsuccessCode
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.UserGroupMappers.toModel
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.UserGroupMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.dtos.*
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.DeleteUserGroupErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUsergroupDetailsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.UserMappers.toModels
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroup
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupDetails
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupSummary
import org.astu.infrastructure.GlobalDIContext

class ApiUserGroupDataSource : UserGroupDataSource {
    private val client: HttpClient by GlobalDIContext.inject<HttpClient>()


    override suspend fun getDetails(id: Uuid): ContentWithError<UserGroupDetails, GetUsergroupDetailsErrors> {
        val response = client.get("api/usergroups/get-details/$id")

        if (!response.status.isSuccess())
            return ContentWithError(null, error = readUnsuccessCode<GetUsergroupDetailsErrors>(response))

        val dto = response.body<UserGroupDetailsDto>()
        return ContentWithError(dto.toModel(), error = null)
    }

    override suspend fun getUserGroupHierarchy(): ContentWithError<UserGroupHierarchy, GetUserHierarchyErrors> {
        val response = client.get("api/usergroups/get-owned-hierarchy")

        if (!response.status.isSuccess())
            return ContentWithError(null, error = readUnsuccessCode<GetUserHierarchyErrors>(response))

        val hierarchyDto = response.body<UserGroupHierarchyDto>()
        val hierarchy = mapHierarchy(hierarchyDto)
        return ContentWithError(hierarchy, error = null)

    }

    override suspend fun getUserGroupList(): ContentWithError<List<UserGroupSummary>, GetUserListErrors> {
        val response = client.get("api/usergroups/get-owned-list")

        if (!response.status.isSuccess())
            return ContentWithError(null, error = readUnsuccessCode<GetUserListErrors>(response))

        val dto = response.body<Array<UserGroupSummaryDto>>()
        val list = dto.toModels()
        return ContentWithError(list, error = null)
    }

    override suspend fun delete(id: Uuid): DeleteUserGroupErrors? {
        val dto = "\"$id\""
        val response = client.delete("api/usergroups/delete") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess())
            return readUnsuccessCode<DeleteUserGroupErrors>(response)

        return null
    }



    // todo вынести в класс с мапперами
    private fun mapHierarchy(dto: UserGroupHierarchyDto): UserGroupHierarchy {
        val userGroupPlainMap = mapUserGroupsToPlainMap(dto.userGroups)
        val roots = dto.roots.map { mapUserGroup(it, userGroupPlainMap) }
        return UserGroupHierarchy(roots)
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
        return UserGroup(uuidFrom(dto.summary.id), dto.summary.name, dto.summary.adminName, mutableListOf(), dto.members.toModels())
    }
}