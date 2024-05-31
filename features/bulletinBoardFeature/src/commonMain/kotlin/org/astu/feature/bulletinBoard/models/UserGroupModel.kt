package org.astu.feature.bulletinBoard.models

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.*
import org.astu.feature.bulletinBoard.models.entities.audience.*
import org.astu.feature.bulletinBoard.models.repositories.UserGroupRepository

class UserGroupModel {
    private val repository: UserGroupRepository = UserGroupRepository()

    suspend fun getCreateContent(): ContentWithError<ContentForUserGroupCreation, GetUsergroupCreateContentErrors> {
        return repository.getCreateContent()
    }

    suspend fun create(content: CreateUserGroup): CreateUserGroupErrors? {
        return repository.create(content)
    }

    suspend fun getUserGroupHierarchy(): ContentWithError<UserGroupHierarchy, GetUserHierarchyErrors> {
        return repository.getUserGroupHierarchy()
    }

    suspend fun getUserGroupList(): ContentWithError<List<UserGroupSummary>, GetUserListErrors> {
        return repository.getUserGroupList()
    }

    suspend fun getDetails(id: Uuid): ContentWithError<UserGroupDetails, GetUsergroupDetailsErrors> {
        return repository.getDetails(id)
    }

    suspend fun delete(id: Uuid): DeleteUserGroupErrors? {
        return repository.delete(id)
    }
}