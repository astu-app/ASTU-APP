package org.astu.app.entities.bulletInBoard.announcement.summary

import com.benasher44.uuid.Uuid
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase

class AnnouncementSummaryContent(
    val id: Uuid,
    val author: String,
    val publicationTime: String,
    val text: String,
    val viewed: Int,
    val audienceSize: Int,
    val attachments: List<AttachmentBase>? = null,
)