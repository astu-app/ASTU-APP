package org.astu.feature.bulletinBoard.models.entities.announcements

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import org.astu.feature.bulletinBoard.models.entities.audience.CheckableUser


class AnnouncementDetails (
    val id: Uuid,
    val content: String,
    val authorName: String,
    val viewsCount: Int,
    val audienceSize: Int,
//    val audience: IAudienceNode,
    val audience: List<CheckableUser>,
    val surveys: List<SurveyDetails>,
    val publishedAt: LocalDateTime?,
    val hiddenAt: LocalDateTime?,
    val delayedHidingAt: LocalDateTime?,
    val delayedPublishingAt: LocalDateTime?,
)