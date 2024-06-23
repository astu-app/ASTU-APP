package org.astu.feature.bulletinBoard.models

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.*
import org.astu.feature.bulletinBoard.models.entities.audience.*
import org.astu.feature.bulletinBoard.models.repositories.UserGroupRepository
import org.astu.feature.bulletinBoard.views.entities.userGroups.editing.EditUserGroupContent

class UserGroupModel {
    private val repository: UserGroupRepository = UserGroupRepository()

    suspend fun getCreateContent(rootUserGroupId: Uuid): ContentWithError<ContentForUserGroupCreation, GetUsergroupCreateContentErrors> {
        return repository.getCreateContent(rootUserGroupId)
    }

    suspend fun create(content: CreateUserGroup, rootUserGroupId: Uuid): CreateUserGroupErrors? {
        return repository.create(content, rootUserGroupId)
    }

    suspend fun getUserGroupHierarchy(): ContentWithError<UserGroupHierarchy, GetUserHierarchyErrors> {
        return repository.getUserGroupHierarchy()
    }

//    suspend fun getUserGroupList(): ContentWithError<List<UserGroupSummary>, GetUserListErrors> { // remove
//        return repository.getUserGroupList()
//    }

    suspend fun getDetails(id: Uuid, rootUserGroupId: Uuid): ContentWithError<UserGroupDetails, GetUsergroupDetailsErrors> {
        return repository.getDetails(id, rootUserGroupId)
    }

    suspend fun getUpdateContent(id: Uuid, rootUserGroupId: Uuid): ContentWithError<ContentForUserGroupEditing, ContentForUserGroupEditingErrors> {
        return repository.getUpdateContent(id, rootUserGroupId)
    }

    fun canUpdate(content: EditUserGroupContent?): Boolean {
        return content != null && content.canEdit()
    }

    suspend fun update(content: UpdateUserGroup, rootUserGroupId: Uuid): UpdateUsergroupErrors? {
        return repository.update(content, rootUserGroupId)
    }

    suspend fun delete(id: Uuid, rootUserGroupId: Uuid): DeleteUserGroupErrors? {
        return repository.delete(id, rootUserGroupId)
    }
}