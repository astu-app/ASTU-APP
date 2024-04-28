package org.astu.feature.bulletinBoard.models.dataSoruces.userGroups.announcementAudience

import org.astu.feature.bulletinBoard.models.dataSoruces.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.userGroups.common.responses.GetUserHierarchyResponses
import org.astu.feature.bulletinBoard.models.entities.audience.AudienceHierarchy

interface AnnouncementAudienceDataSource {
    suspend fun getAudienceForCreation(): ContentWithError<AudienceHierarchy, GetUserHierarchyResponses>
}