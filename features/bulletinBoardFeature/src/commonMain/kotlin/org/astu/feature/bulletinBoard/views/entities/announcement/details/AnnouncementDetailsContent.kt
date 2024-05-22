package org.astu.feature.bulletinBoard.views.entities.announcement.details

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase
import org.astu.feature.bulletinBoard.views.entities.users.CheckableUserSummary

class AnnouncementDetailsContent(
    val id: Uuid,
    val author: String,
    val publicationTime: String,
    val viewed: Int,
    val viewedPercent: Int,
    val audienceSize: Int,
    val text: String,
    val attachments: List<AttachmentContentBase>? = null,
    val audience : List<CheckableUserSummary>
)