package org.astu.app.components.bulletinBoard.attachments.surveys.common.models

import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentBase
import org.astu.app.components.bulletinBoard.attachments.common.models.AttachmentType
import org.astu.app.components.bulletinBoard.attachments.surveys.questions.models.QuestionContentBase

class SurveyContent(val questions: List<QuestionContentBase>) : AttachmentBase() {
    override val type: AttachmentType = AttachmentType.SURVEY
}