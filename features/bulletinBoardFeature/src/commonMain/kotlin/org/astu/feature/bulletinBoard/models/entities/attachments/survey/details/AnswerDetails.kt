package org.astu.feature.bulletinBoard.models.entities.attachments.survey.details

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.entities.audience.User

data class AnswerDetails(
    val id: Uuid,
    val serial: Int,
    val content: String,
    val voters: List<User>?,
    val votersAmount: Int,
    val canVote: Boolean,
)