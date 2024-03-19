package org.astu.app.components.bulletinBoard.announcements.details.models

import com.benasher44.uuid.Uuid
import org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph.NodeBase
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase

class AnnouncementDetailsContent(
    val id: Uuid,
    val author: String,
    val publicationTime: String,
    val viewed: Int,
    val viewedPercent: Int,
    val audienceSize: Int,
    val text: String,
    val rootAudienceNode: NodeBase,
    val attachments: List<AttachmentBase>? = null,
)