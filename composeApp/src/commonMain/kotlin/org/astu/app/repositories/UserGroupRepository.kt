package org.astu.app.repositories

import org.astu.app.dataSources.bulletInBoard.common.responses.ContentWithError
import org.astu.app.dataSources.bulletInBoard.userGroups.announcementAudience.ApiAnnouncementAudienceDataSource
import org.astu.app.dataSources.bulletInBoard.userGroups.common.responses.GetUserHierarchyResponses
import org.astu.app.models.bulletInBoard.entities.audience.AudienceHierarchy

class UserGroupRepository {
    private val announcementAudienceSource = ApiAnnouncementAudienceDataSource()

    suspend fun getAudience(): ContentWithError<AudienceHierarchy, GetUserHierarchyResponses> {
        return announcementAudienceSource.getAudienceForCreation()
    }
}