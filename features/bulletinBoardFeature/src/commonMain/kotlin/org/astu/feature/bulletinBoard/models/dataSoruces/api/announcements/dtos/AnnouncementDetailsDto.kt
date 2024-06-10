package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.dtos.details.SurveyDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.users.dtos.CheckableUserSummaryDto

@Serializable
data class AnnouncementDetailsDto (
    val id: String,
    val content: String,
    val authorName: String,
    val viewsCount: Int,
    val audienceSize: Int,
    val surveys: List<SurveyDetailsDto>?,
    val publishedAt: LocalDateTime?,
    val hiddenAt: LocalDateTime?,
    val delayedHidingAt: LocalDateTime?,
    val delayedPublishingAt: LocalDateTime?,
    val audience: List<CheckableUserSummaryDto>,
)