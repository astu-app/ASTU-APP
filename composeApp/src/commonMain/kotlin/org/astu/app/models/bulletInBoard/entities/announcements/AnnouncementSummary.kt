package org.astu.app.models.bulletInBoard.entities.announcements

import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import org.astu.app.models.bulletInBoard.entities.attachments.file.File
import org.astu.app.models.bulletInBoard.entities.attachments.survey.details.SurveyDetails

data class AnnouncementSummary (
    val id: Uuid,
    val author: String,
    val publicationTime: LocalDateTime,
    val text: String,
    val viewed: Int,
    val audienceSize: Int,
    val files: List<File>?,
    val surveys: List<SurveyDetails>?,
)