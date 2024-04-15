package org.astu.app.models.bulletInBoard.entities.announcements

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import org.astu.app.models.bulletInBoard.entities.attachments.file.File
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.SurveyDetails
import org.astu.app.models.bulletInBoard.entities.audience.User

class AnnouncementDetails (
    val id: Uuid,
    val content: String,
    val authorName: String,
    val viewsCount: Int,
    val audienceSize: Int,
//    val audience: IAudienceNode,
    val audience: List<User>,
    val files: List<File>,
    val surveys: List<SurveyDetails>,
    val publishedAt: LocalDateTime?,
    val hiddenAt: LocalDateTime?,
    val delayedHidingAt: LocalDateTime?,
    val delayedPublishingAt: LocalDateTime?,
    // todo добавить категории объявлений
)