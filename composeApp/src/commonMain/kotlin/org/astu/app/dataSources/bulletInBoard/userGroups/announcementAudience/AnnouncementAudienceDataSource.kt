package org.astu.app.dataSources.bulletInBoard.userGroups.announcementAudience

import org.astu.app.dataSources.bulletInBoard.common.responses.ContentWithError
import org.astu.app.dataSources.bulletInBoard.userGroups.common.responses.GetUserHierarchyResponses
import org.astu.app.models.bulletInBoard.entities.audience.AudienceHierarchy

interface AnnouncementAudienceDataSource {
    suspend fun getAudienceForCreation(): ContentWithError<AudienceHierarchy, GetUserHierarchyResponses>
}