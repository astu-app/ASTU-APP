package org.astu.feature.bulletinBoard.views.components.attachments.files.models

import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentType

abstract class FileContentBase(
    val name: String,
    val size: String
) : AttachmentContentBase(AttachmentType.FILE)