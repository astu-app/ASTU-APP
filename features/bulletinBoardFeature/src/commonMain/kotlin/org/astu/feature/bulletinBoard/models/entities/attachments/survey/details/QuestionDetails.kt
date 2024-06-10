package org.astu.feature.bulletinBoard.models.entities.attachments.survey.details

import com.benasher44.uuid.Uuid

data class QuestionDetails (
    val id: Uuid,
    val serial: Int,
    val content: String,
    val isMultipleChoiceAllowed: Boolean,
    val answers: List<AnswerDetails>
)