package org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentType
import org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models.QuestionContentBase
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary

abstract class SurveyContentBase(
    val id: Uuid,
    val voters: List<UserSummary>,
    val showVoters: Boolean,
    val questions: List<QuestionContentBase>,
) : AttachmentContentBase(AttachmentType.SURVEY)