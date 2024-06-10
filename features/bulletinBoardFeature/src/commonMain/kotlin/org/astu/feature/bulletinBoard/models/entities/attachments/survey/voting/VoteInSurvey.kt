package org.astu.feature.bulletinBoard.models.entities.attachments.survey.voting

import com.benasher44.uuid.Uuid

data class VoteInSurvey (
    val surveyId: Uuid,
    val questionVotes: List<VoteInQuestion>
)