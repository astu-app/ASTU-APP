package org.astu.feature.bulletinBoard.views.components.attachments.common.models

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider

abstract class AttachmentBase(
    val type: AttachmentType,
    val id: Uuid,
): DefaultModifierProvider, ContentProvider