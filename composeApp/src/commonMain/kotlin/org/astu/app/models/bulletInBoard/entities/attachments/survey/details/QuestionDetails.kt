package org.astu.app.models.bulletInBoard.entities.attachments.survey.details

import com.benasher44.uuid.Uuid

data class QuestionDetails (
    val id: Uuid,
    val content: String,
    val isMultipleChoiceAllowed: Boolean,
    val answers: List<AnswerDetails>
)