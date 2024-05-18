package org.astu.feature.bulletinBoard.models.entities.announcements

import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateSurvey

data class CreateAnnouncement (
    val content: String,
    val userIds: List<String>,
    val delayedPublishingAt: LocalDateTime?,
    val delayedHidingAt: LocalDateTime?,

    val survey: CreateSurvey?,
)