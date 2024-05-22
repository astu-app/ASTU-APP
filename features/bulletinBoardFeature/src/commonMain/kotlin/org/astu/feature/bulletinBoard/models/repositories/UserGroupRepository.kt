package org.astu.feature.bulletinBoard.models.repositories

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.ApiUserGroupDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.DeleteUserGroupErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUsergroupDetailsErrors
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupDetails
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupSummary


class UserGroupRepository {
    private val announcementAudienceSource = ApiUserGroupDataSource()

    suspend fun getUserGroupHierarchy(): ContentWithError<UserGroupHierarchy, GetUserHierarchyErrors> {
        return announcementAudienceSource.getUserGroupHierarchy()
    }

    suspend fun getUserGroupList(): ContentWithError<List<UserGroupSummary>, GetUserListErrors> {
        return announcementAudienceSource.getUserGroupList()
    }

    suspend fun getDetails(id: Uuid): ContentWithError<UserGroupDetails, GetUsergroupDetailsErrors> {
        return announcementAudienceSource.getDetails(id)
    }

    suspend fun delete(id: Uuid): DeleteUserGroupErrors? {
        return announcementAudienceSource.delete(id)
    }
}