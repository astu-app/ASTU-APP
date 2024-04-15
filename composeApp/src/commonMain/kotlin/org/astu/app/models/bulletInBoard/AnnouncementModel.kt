package org.astu.app.models.bulletInBoard

import com.benasher44.uuid.Uuid
import org.astu.app.entities.bulletInBoard.EditAnnouncementContent
import org.astu.app.entities.bulletInBoard.announcement.creation.CreateAnnouncementContent
import org.astu.app.entities.bulletInBoard.announcement.summary.AnnouncementSummaryContent
import org.astu.app.models.bulletInBoard.entities.announcements.AnnouncementDetails
import org.astu.app.models.bulletInBoard.entities.announcements.CreateAnnouncement
import org.astu.app.models.bulletInBoard.updating.AnnouncementCreateValidator
import org.astu.app.models.bulletInBoard.updating.AnnouncementEditValidator
import org.astu.app.repositories.AnnouncementRepository

class AnnouncementModel {
    private val repository: AnnouncementRepository = AnnouncementRepository()


    suspend fun getAnnouncementList(): List<AnnouncementSummaryContent> {
        return repository.loadList()
    }

    fun getDetails(id: Uuid): AnnouncementDetails {
        return repository.loadDetails(id)
    }

    fun create(announcement: CreateAnnouncement) {
        return repository.create(announcement)
    }

    fun getCreateContent(): CreateAnnouncementContent {
        return CreateAnnouncementContent()
    }

    fun canCreate(announcement: CreateAnnouncementContent): Boolean {
        val createValidator = AnnouncementCreateValidator(announcement)
        return createValidator.canCreate()
    }

    fun getEditContent(id: Uuid): EditAnnouncementContent { // todo задействовать репозиторий
        val details = repository.loadDetails(id)
        return EditAnnouncementContent(
            id = id,

        )
    }

    fun canEdit(announcement: EditAnnouncementContent): Boolean {
        val editValidator = AnnouncementEditValidator(announcement)
        return editValidator.canEdit()
    }
}