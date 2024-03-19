package org.astu.app.components.bulletinBoard.attachments.common.models

import org.astu.app.components.bulletinBoard.common.models.ContentProvider
import org.astu.app.components.bulletinBoard.common.models.DefaultModifierProvider

abstract class AttachmentBase(val type: AttachmentType): DefaultModifierProvider, ContentProvider