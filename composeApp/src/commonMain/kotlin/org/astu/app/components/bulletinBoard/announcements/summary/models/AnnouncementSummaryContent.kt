package org.astu.app.components.bulletinBoard.announcements.summary.models

import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase

class AnnouncementSummaryContent(
    val author: String,
    val publicationTime: String,
    val text: String,
    val viewed: Int,
    val audienceSize: Int,
    val attachments: List<AttachmentBase>? = null,
)