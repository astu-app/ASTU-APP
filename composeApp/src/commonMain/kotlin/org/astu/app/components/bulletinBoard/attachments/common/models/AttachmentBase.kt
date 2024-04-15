package org.astu.app.components.bulletinBoard.attachments.common.models

import com.benasher44.uuid.Uuid
import org.astu.app.components.bulletinBoard.common.models.ContentProvider
import org.astu.app.components.bulletinBoard.common.models.DefaultModifierProvider

abstract class AttachmentBase(
    val type: AttachmentType,
    val id: Uuid,
): DefaultModifierProvider, ContentProvider