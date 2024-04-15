package org.astu.app.entities.bulletInBoard.announcement.details

import com.benasher44.uuid.Uuid
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.components.bulletinBoard.common.models.UserSummary

class AnnouncementDetailsContent(
    val id: Uuid,
    val author: String,
    val publicationTime: String,
    val viewed: Int,
    val viewedPercent: Int,
    val audienceSize: Int,
    val text: String,
//    val rootAudienceNode: INode, // remove
    val attachments: List<AttachmentBase>? = null,
    val audience : List<UserSummary>
)