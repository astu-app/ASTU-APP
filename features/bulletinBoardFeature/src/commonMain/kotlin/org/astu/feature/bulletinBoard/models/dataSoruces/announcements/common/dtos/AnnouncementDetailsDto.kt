package org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.dtos

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.astu.feature.bulletinBoard.models.dataSoruces.attachments.files.dtos.FileSummaryDto
import org.astu.feature.bulletinBoard.models.dataSoruces.attachments.surveys.dtos.SurveyDetailsDto
import org.astu.feature.bulletinBoard.models.dataSoruces.dtos.UserSummaryDto

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
    val categories: List<org.astu.feature.bulletinBoard.models.dataSoruces.announcements.common.dtos.AnnouncementCategoryDetailsDto>,
)