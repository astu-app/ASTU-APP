package org.astu.app.components.bulletinBoard.attachments.files

import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentType

class FileSummary(
    val name: String,
    val size: String,
) : AttachmentBase() {
    override val type: AttachmentType = AttachmentType.FILE
}