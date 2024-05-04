package org.astu.feature.bulletinBoard.views.entities.announcement.summary

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase

class AnnouncementSummaryContent(
    val id: Uuid,
    val author: String,
    val publicationTime: LocalDateTime,
    val text: String,
    val viewed: Int,
    val audienceSize: Int,
    val attachments: List<AttachmentContentBase> = emptyList(),
)