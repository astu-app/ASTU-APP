package org.astu.feature.bulletinBoard.models.repositories

import org.astu.feature.bulletinBoard.models.dataSoruces.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.userGroups.announcementAudience.ApiAnnouncementAudienceDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.userGroups.common.responses.GetUserHierarchyResponses
import org.astu.feature.bulletinBoard.models.entities.audience.AudienceHierarchy


class UserGroupRepository {
    private val announcementAudienceSource = ApiAnnouncementAudienceDataSource()

    suspend fun getAudience(): ContentWithError<AudienceHierarchy, GetUserHierarchyResponses> {
        return announcementAudienceSource.getAudienceForCreation()
    }
}