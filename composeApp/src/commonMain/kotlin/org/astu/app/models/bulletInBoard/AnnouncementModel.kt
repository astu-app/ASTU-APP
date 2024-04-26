package org.astu.app.models.bulletInBoard

import com.benasher44.uuid.Uuid
import org.astu.app.dataSources.bulletInBoard.announcements.common.responses.CreateAnnouncementErrors
import org.astu.app.dataSources.bulletInBoard.announcements.common.responses.GetAnnouncementDetailsErrors
import org.astu.app.dataSources.bulletInBoard.announcements.common.responses.GetPostedAnnouncementListErrors
import org.astu.app.dataSources.bulletInBoard.common.responses.ContentWithError
import org.astu.app.dataSources.bulletInBoard.userGroups.common.responses.GetUserHierarchyResponses
import org.astu.app.entities.bulletInBoard.EditAnnouncementContent
import org.astu.app.entities.bulletInBoard.announcement.creation.CreateAnnouncementContent
import org.astu.app.entities.bulletInBoard.announcement.summary.AnnouncementSummaryContent
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementDetails
import org.astu.app.models.bulletInBoard.entities.announcements.CreateAnnouncement
import org.astu.app.models.bulletInBoard.updating.AnnouncementCreateValidator
import org.astu.app.models.bulletInBoard.updating.AnnouncementEditValidator
import org.astu.app.repositories.AnnouncementRepository
import org.astu.app.repositories.UserGroupRepository

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
        val editValidator = AnnouncementEditValidator(announcement)
        return editValidator.canEdit()
    }
}