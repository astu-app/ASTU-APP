package org.astu.feature.bulletinBoard.models.entities.announcements

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import org.astu.feature.bulletinBoard.models.entities.audience.User

data class AnnouncementSummary (
    val id: Uuid,
    val author: User,
    val publicationTime: LocalDateTime?,
    val hidingTime: LocalDateTime?,
    val delayedPublicationTime: LocalDateTime?,
    val delayedHidingTime: LocalDateTime?,
    val text: String,
    val viewed: Int,
    val audienceSize: Int,
    val surveys: List<SurveyDetails>?,
)