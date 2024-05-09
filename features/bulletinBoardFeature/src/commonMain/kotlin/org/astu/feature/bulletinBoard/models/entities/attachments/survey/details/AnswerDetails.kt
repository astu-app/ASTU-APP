package org.astu.feature.bulletinBoard.models.entities.attachments.survey.details

import com.benasher44.uuid.Uuid

data class AnswerDetails(
    val id: Uuid,
    val serial: Int,
    val content: String,
    val votersAmount: Int,
    val canVote: Boolean,
)