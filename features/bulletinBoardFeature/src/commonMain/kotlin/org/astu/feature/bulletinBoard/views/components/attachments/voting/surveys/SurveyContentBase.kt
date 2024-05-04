package org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys

import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentType
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.QuestionContentBase

abstract class SurveyContentBase(
    val questions: List<QuestionContentBase>,
) : AttachmentContentBase(AttachmentType.SURVEY)