package org.astu.feature.bulletinBoard.views.components.attachments.common.models

import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider

abstract class AttachmentContentBase(
    val type: AttachmentType,
): DefaultModifierProvider, ContentProvider