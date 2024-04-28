package org.astu.feature.bulletinBoard.views.entities.announcement.details

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentBase
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary

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