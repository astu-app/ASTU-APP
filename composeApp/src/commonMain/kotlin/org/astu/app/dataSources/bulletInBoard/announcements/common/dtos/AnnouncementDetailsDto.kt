package org.astu.app.dataSources.bulletInBoard.announcements.common.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.astu.app.dataSources.bulletInBoard.attachments.files.dtos.FileSummaryDto
import org.astu.app.dataSources.bulletInBoard.attachments.surveys.dtos.SurveyDetailsDto

@Serializable
data class AnnouncementDetailsDto (
    val id: String,
    val content: String,
    val authorName: String,
    val viewsCount: Int,
    val audienceSize: Int,
    val files: List<FileSummaryDto>?,
    val surveys: List<SurveyDetailsDto>?,
    val publishedAt: LocalDateTime?,
    val hiddenAt: LocalDateTime?,
    val delayedHidingAt: LocalDateTime?,
    val delayedPublishingAt: LocalDateTime?,
    val audience: List<UserSummaryDto>,
    val categories: List<AnnouncementCategoryDetailsDto>,
)