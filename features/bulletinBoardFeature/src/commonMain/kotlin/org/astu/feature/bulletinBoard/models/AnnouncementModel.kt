package org.astu.feature.bulletinBoard.models

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.CreateAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.EditAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetAnnouncementDetailsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetPostedAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.responses.GetUserHierarchyResponses
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementDetails
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.feature.bulletinBoard.models.entities.announcements.EditAnnouncement
import org.astu.feature.bulletinBoard.models.entities.audience.AudienceHierarchy
import org.astu.feature.bulletinBoard.models.repositories.AnnouncementRepository
import org.astu.feature.bulletinBoard.models.repositories.UserGroupRepository
import org.astu.feature.bulletinBoard.models.services.announcements.AnnouncementCreateValidator
import org.astu.feature.bulletinBoard.models.services.announcements.AnnouncementEditValidator
import org.astu.feature.bulletinBoard.views.entities.EditAnnouncementContent
import org.astu.feature.bulletinBoard.views.entities.announcement.creation.CreateAnnouncementContent
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent

class AnnouncementModel {
    private val announcementRepository: AnnouncementRepository = AnnouncementRepository()
    private val userGroupRepository: UserGroupRepository = UserGroupRepository()


    suspend fun getAnnouncementList(): ContentWithError<List<AnnouncementSummaryContent>, GetPostedAnnouncementListErrors> {
        return announcementRepository.loadList()
    }

    suspend fun getDetails(id: Uuid): ContentWithError<AnnouncementDetails, GetAnnouncementDetailsErrors> {
        return announcementRepository.loadDetails(id)
    }

    suspend fun create(announcement: CreateAnnouncement): CreateAnnouncementErrors? {
        return announcementRepository.create(announcement)
    }

    suspend fun getAudienceHierarchy(): ContentWithError<AudienceHierarchy, GetUserHierarchyResponses> {
        val audience = userGroupRepository.getAudience()
        if (!audience.isContentValid) {
            return ContentWithError(null, audience.error)
        }

        return ContentWithError(audience.content, error = null)
    }

    fun canCreate(announcement: CreateAnnouncementContent?): Boolean {
        if (announcement == null)
            return false

        val createValidator = AnnouncementCreateValidator(announcement)
        return createValidator.canCreate()
    }

    suspend fun getEditContent(id: Uuid): ContentWithError<ContentForAnnouncementEditing, EditAnnouncementErrors> {
        return announcementRepository.loadEditContent(id)
    }

    fun canEdit(announcement: EditAnnouncementContent?): Boolean {
        if (announcement == null)
            return false

        val editValidator = AnnouncementEditValidator(announcement)
        return editValidator.canEdit()
    }

    suspend fun edit(announcement: EditAnnouncement): EditAnnouncementErrors? {
        return announcementRepository.edit(announcement)
    }
}