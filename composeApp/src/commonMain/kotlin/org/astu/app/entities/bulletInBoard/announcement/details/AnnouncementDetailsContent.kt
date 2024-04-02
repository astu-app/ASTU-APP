package org.astu.app.entities.bulletInBoard.announcement.details

import com.benasher44.uuid.Uuid
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.entities.bulletInBoard.audienceGraph.INode

class AnnouncementDetailsContent(
    val id: Uuid,
    val author: String,
    val publicationTime: String,
    val viewed: Int,
    val viewedPercent: Int,
    val audienceSize: Int,
    val text: String,
    val rootAudienceNode: INode,
    val attachments: List<AttachmentBase>? = null,
)