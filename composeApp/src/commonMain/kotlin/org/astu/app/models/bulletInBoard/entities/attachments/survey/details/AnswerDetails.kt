package org.astu.app.models.bulletInBoard.entities.attachments.survey.details

import com.benasher44.uuid.Uuid

data class AnswerDetails(
    val id: Uuid,
    val serial: Int,
    val content: String,
    val votersAmount: Int,
)