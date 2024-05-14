package org.astu.feature.bulletinBoard.models.entities.announcements

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.models.entities.attachments.file.details.File
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.details.SurveyDetails
import org.astu.feature.bulletinBoard.models.entities.audience.UserGroupHierarchy

/**
 * Класс, предоставляющий данные для редактирования объявления
 */
data class ContentForAnnouncementEditing (
    val id: Uuid,
    val authorName: String,
    val content: String,
    val viewsCount: Int,
    val audienceSize: Int,
    val audienceHierarchy: UserGroupHierarchy,
    val files: List<File>,
    val surveys: List<SurveyDetails>?,
    val publishedAt: LocalDateTime?,
    val hiddenAt: LocalDateTime?,
    val delayedHidingAt: LocalDateTime?,
    val delayedPublishingAt: LocalDateTime?,
    // todo добавить категории объявления
)