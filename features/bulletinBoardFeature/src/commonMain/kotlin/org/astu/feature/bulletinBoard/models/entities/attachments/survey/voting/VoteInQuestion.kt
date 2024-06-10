package org.astu.feature.bulletinBoard.models.entities.attachments.survey.voting

import com.benasher44.uuid.Uuid

data class VoteInQuestion (
    val questionId: Uuid,
    val answerIds: List<Uuid>
)