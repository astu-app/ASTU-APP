package org.astu.app.components.bulletinBoard.attachments

import androidx.compose.runtime.Composable
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase

@Composable
fun Attachment(attachment: AttachmentBase) {
    attachment.Content(attachment.getDefaultModifier())
}