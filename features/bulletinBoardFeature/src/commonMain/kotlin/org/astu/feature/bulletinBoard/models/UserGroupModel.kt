package org.astu.feature.bulletinBoard.models

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.DeleteUserGroupErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUsergroupDetailsErrors
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupDetails
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupSummary
import org.astu.feature.bulletinBoard.models.repositories.UserGroupRepository

class UserGroupModel {
    private val repository: UserGroupRepository = UserGroupRepository()

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