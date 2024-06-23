package org.astu.feature.bulletinBoard.models.dataSoruces

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.*
import org.astu.feature.bulletinBoard.models.entities.audience.*

interface UserGroupDataSource {
    suspend fun getCreateContent(rootUserGroupId: Uuid): ContentWithError<ContentForUserGroupCreation, GetUsergroupCreateContentErrors>
    suspend fun create(content: CreateUserGroup, rootUserGroupId: Uuid): CreateUserGroupErrors?
    suspend fun getDetails(id: Uuid, rootUserGroupId: Uuid): ContentWithError<UserGroupDetails, GetUsergroupDetailsErrors>
    suspend fun getUpdateContent(id: Uuid, rootUserGroupId: Uuid): ContentWithError<ContentForUserGroupEditing, ContentForUserGroupEditingErrors>
    suspend fun update(content: UpdateUserGroup, rootUserGroupId: Uuid): UpdateUsergroupErrors?
    suspend fun getUserGroupHierarchy(): ContentWithError<UserGroupHierarchy, GetUserHierarchyErrors>
    suspend fun getUserGroupList(): ContentWithError<List<UserGroupSummary>, GetUserListErrors>
    suspend fun delete(id: Uuid, rootUserGroupId: Uuid): DeleteUserGroupErrors?
}