package org.astu.feature.bulletinBoard.views.components.attachments

import androidx.compose.runtime.Composable
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentBase

@Composable
fun Attachment(attachment: AttachmentBase) {
    attachment.Content(attachment.getDefaultModifier())
}