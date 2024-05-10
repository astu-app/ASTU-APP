package org.astu.feature.bulletinBoard.models.entities.announcements

import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.models.entities.attachments.file.creation.CreateFile
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateSurvey

data class CreateAnnouncement (
    val content: String,
    val userIds: List<String>,
    val delayedPublishingAt: LocalDateTime?,
    val delayedHidingAt: LocalDateTime?,
    val categoryIds: List<String>,

    val survey: CreateSurvey?,
    val files: List<CreateFile>?,
)