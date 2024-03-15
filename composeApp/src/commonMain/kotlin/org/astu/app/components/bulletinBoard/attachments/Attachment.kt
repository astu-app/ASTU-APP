package org.astu.app.components.bulletinBoard.attachments

import androidx.compose.runtime.Composable
import org.astu.app.components.bulletinBoard.announcements.File
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentType
import org.astu.app.components.bulletinBoard.attachments.files.FileSummary
import org.astu.app.components.bulletinBoard.attachments.surveys.Survey
import org.astu.app.components.bulletinBoard.attachments.surveys.common.models.SurveyContent

@Composable
fun Attachment(attachment: AttachmentBase) {
    if (attachment.type == AttachmentType.FILE) {
        File(attachment as FileSummary)
    } else if (attachment.type == AttachmentType.SURVEY) {
        Survey(attachment as SurveyContent)
    }
}