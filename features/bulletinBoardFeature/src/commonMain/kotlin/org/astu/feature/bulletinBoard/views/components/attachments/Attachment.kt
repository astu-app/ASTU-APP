package org.astu.feature.bulletinBoard.views.components.attachments

import androidx.compose.runtime.Composable
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase

@Composable
fun Attachment(attachment: AttachmentContentBase) {
    attachment.Content(attachment.getDefaultModifier())
}