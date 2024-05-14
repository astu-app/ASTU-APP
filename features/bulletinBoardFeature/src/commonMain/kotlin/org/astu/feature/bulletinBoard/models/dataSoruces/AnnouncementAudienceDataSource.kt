package org.astu.feature.bulletinBoard.models.dataSoruces

import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyResponses
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy

interface AnnouncementAudienceDataSource {
    suspend fun getAudienceForCreation(): ContentWithError<UserGroupHierarchy, GetUserHierarchyResponses>
}