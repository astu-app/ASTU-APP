package org.astu.feature.bulletinBoard.models

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.CreateAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.GetAnnouncementDetailsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.responses.GetPostedAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.userGroups.common.responses.GetUserHierarchyResponses
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementDetails
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.feature.bulletinBoard.models.repositories.AnnouncementRepository
import org.astu.feature.bulletinBoard.models.repositories.UserGroupRepository
import org.astu.feature.bulletinBoard.models.updating.AnnouncementCreateValidator
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

    suspend fun getCreateContent(): ContentWithError<CreateAnnouncementContent, GetUserHierarchyResponses> {
        val audience = userGroupRepository.getAudience()
        if (!audience.isContentValid) {
            return ContentWithError(null, audience.error)
        }

        return ContentWithError(CreateAnnouncementContent(audience.content!!), error = null)
    }

    fun canCreate(announcement: CreateAnnouncementContent?): Boolean {
        if (announcement == null)
            return false

        val createValidator = AnnouncementCreateValidator(announcement)
        return createValidator.canCreate()
    }

    suspend fun getEditContent(id: Uuid): EditAnnouncementContent { // todo задействовать репозиторий
        val details = announcementRepository.loadDetails(id)
        return EditAnnouncementContent(
            id = id,

        )
    }

    fun canEdit(announcement: EditAnnouncementContent): Boolean {
        val editValidator = org.astu.feature.bulletinBoard.models.updating.AnnouncementEditValidator(announcement)
        return editValidator.canEdit()
    }
}