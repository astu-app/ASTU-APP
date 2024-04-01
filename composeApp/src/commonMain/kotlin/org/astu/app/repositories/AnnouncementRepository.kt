package org.astu.app.repositories

import androidx.compose.runtime.mutableStateOf
import com.benasher44.uuid.Uuid
import org.astu.app.components.bulletinBoard.announcements.details.models.AnnouncementDetailsContent
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.components.bulletinBoard.attachments.files.models.FileDownloadState
import org.astu.app.components.bulletinBoard.attachments.files.models.FileSummary
import org.astu.app.components.bulletinBoard.common.models.UserGroupStorage.makeStaticAudience

class AnnouncementRepository {
    fun loadDetails(id: Uuid): AnnouncementDetailsContent {
        return AnnouncementDetailsContent(
            id = id,
            author = "Белов Сергей Валерьевич",
            publicationTime = "Опубликовано 15 фев 15:50",
            viewed = 145,
            viewedPercent = 48,
            audienceSize = 300,
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. ",
            rootAudienceNode = makeStaticAudience(),
            attachments = makeAttachments(),
        )
    }

    fun create() {

    }

    fun update() {

    }



    private fun makeAttachments(): List<AttachmentBase> {
        val attachments = mutableListOf<AttachmentBase>(
            FileSummary("Документ.docx", "20 мб", mutableStateOf(FileDownloadState.DOWNLOADED)),
            FileSummary("Презентация.pptx", "20 мб", mutableStateOf(FileDownloadState.DOWNLOADING)),
            FileSummary("Таблица.xlsx", "20 мб", mutableStateOf(FileDownloadState.NOT_DOWNLOADED)),
        )

        return attachments
    }
}