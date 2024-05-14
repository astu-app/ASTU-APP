package org.astu.feature.bulletinBoard.models

import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyResponses
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy
import org.astu.feature.bulletinBoard.models.repositories.UserGroupRepository

class UserGroupsModel {
    private val repository: UserGroupRepository = UserGroupRepository()

    suspend fun getUserGroupList(): ContentWithError<UserGroupHierarchy, GetUserHierarchyResponses> {
        return repository.getAudience()
    }
}