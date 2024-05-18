package org.astu.feature.bulletinBoard.models.entities.announcements

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails

data class AnnouncementSummary (
    val id: Uuid,
    val author: String,
    val publicationTime: LocalDateTime?,
    val text: String,
    val viewed: Int,
    val audienceSize: Int,
    val surveys: List<SurveyDetails>?,
)