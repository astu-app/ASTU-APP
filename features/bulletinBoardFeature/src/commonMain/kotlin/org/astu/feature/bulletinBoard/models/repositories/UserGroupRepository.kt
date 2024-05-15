package org.astu.feature.bulletinBoard.models.repositories

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.ApiUserGroupDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.DeleteUserGroupErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUsergroupDetailsErrors
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupDetails
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy


class UserGroupRepository {
    private val announcementAudienceSource = ApiUserGroupDataSource()

    suspend fun getAudience(): ContentWithError<UserGroupHierarchy, GetUserHierarchyErrors> {
        return announcementAudienceSource.getAudienceForCreation()
    }

    suspend fun getDetails(id: Uuid): ContentWithError<UserGroupDetails, GetUsergroupDetailsErrors> {
        return announcementAudienceSource.getDetails(id)
    }

    suspend fun delete(id: Uuid): DeleteUserGroupErrors? {
        return announcementAudienceSource.delete(id)
    }
}