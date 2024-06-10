package org.astu.feature.bulletinBoard.models.dataSoruces

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.*
import org.astu.feature.bulletinBoard.models.entities.audience.*

interface UserGroupDataSource {
    suspend fun getCreateContent(): ContentWithError<ContentForUserGroupCreation, GetUsergroupCreateContentErrors>
    suspend fun create(content: CreateUserGroup): CreateUserGroupErrors?
    suspend fun getDetails(id: Uuid): ContentWithError<UserGroupDetails, GetUsergroupDetailsErrors>
    suspend fun getUpdateContent(id: Uuid): ContentWithError<ContentForUserGroupEditing, ContentForUserGroupEditingErrors>
    suspend fun update(content: UpdateUserGroup): UpdateUsergroupErrors?
    suspend fun getUserGroupHierarchy(): ContentWithError<UserGroupHierarchy, GetUserHierarchyErrors>
    suspend fun getUserGroupList(): ContentWithError<List<UserGroupSummary>, GetUserListErrors>
    suspend fun delete(id: Uuid): DeleteUserGroupErrors?
}