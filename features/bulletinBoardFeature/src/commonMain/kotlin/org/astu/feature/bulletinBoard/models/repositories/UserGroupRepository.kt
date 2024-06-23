package org.astu.feature.bulletinBoard.models.repositories

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.ApiUserGroupDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.*
import org.astu.feature.bulletinBoard.models.entities.audience.*
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.GatewayServiceConfig


class UserGroupRepository {
    private val config by GlobalDIContext.inject<GatewayServiceConfig>()
    private val userGroupDataSource = ApiUserGroupDataSource(config.url)

    suspend fun getCreateContent(rootUserGroupId: Uuid): ContentWithError<ContentForUserGroupCreation, GetUsergroupCreateContentErrors> {
        return userGroupDataSource.getCreateContent(rootUserGroupId)
    }

    suspend fun create(content: CreateUserGroup, rootUserGroupId: Uuid): CreateUserGroupErrors? {
        return userGroupDataSource.create(content, rootUserGroupId)
    }

    suspend fun getUserGroupHierarchy(): ContentWithError<UserGroupHierarchy, GetUserHierarchyErrors> {
        return userGroupDataSource.getUserGroupHierarchy()
    }

//    suspend fun getUserGroupList(): ContentWithError<List<UserGroupSummary>, GetUserListErrors> { // remove
//        return userGroupDataSource.getUserGroupList()
//    }

    suspend fun getDetails(id: Uuid, rootUserGroupId: Uuid): ContentWithError<UserGroupDetails, GetUsergroupDetailsErrors> {
        return userGroupDataSource.getDetails(id, rootUserGroupId)
    }

    suspend fun getUpdateContent(id: Uuid, rootUserGroupId: Uuid): ContentWithError<ContentForUserGroupEditing, ContentForUserGroupEditingErrors> {
        return userGroupDataSource.getUpdateContent(id, rootUserGroupId)
    }

    suspend fun update(content: UpdateUserGroup, rootUserGroupId: Uuid): UpdateUsergroupErrors? {
        return userGroupDataSource.update(content, rootUserGroupId)
    }

    suspend fun delete(id: Uuid, rootUserGroupId: Uuid): DeleteUserGroupErrors? {
        return userGroupDataSource.delete(id, rootUserGroupId)
    }
}