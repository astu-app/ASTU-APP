package org.astu.app.models.bulletInBoard

import com.benasher44.uuid.Uuid
import org.astu.app.components.bulletinBoard.announcements.details.models.AnnouncementDetailsContent
import org.astu.app.entities.bulletInBoard.EditAnnouncementContent
import org.astu.app.models.bulletInBoard.updating.AnnouncementEditValidator
import org.astu.app.repositories.AnnouncementRepository

class AnnouncementModel {
    private val repository: AnnouncementRepository = AnnouncementRepository()


    fun getDetails(id: Uuid): AnnouncementDetailsContent {
        return repository.loadDetails(id)
    }

    fun create() {

    }

//    fun update(updater: AnnouncementUpdateCollector) {
//
//    }

    fun getEditContent(id: Uuid): EditAnnouncementContent {
        return EditAnnouncementContent()
    }

    fun canEdit(announcement: EditAnnouncementContent): Boolean {
        val editValidator = AnnouncementEditValidator(announcement)
        return editValidator.canEdit()
    }
}