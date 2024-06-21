package org.astu.feature.bulletinBoard.views.entities.announcement.summary

import androidx.compose.runtime.MutableState
import com.benasher44.uuid.Uuid
import kotlinx.datetime.LocalDateTime
import org.astu.feature.bulletinBoard.views.components.attachments.common.models.AttachmentContentBase
import org.astu.feature.bulletinBoard.views.components.attachments.voting.surveys.AttachedSurveyContent
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary

data class AnnouncementSummaryContent(
    val id: Uuid,
    val author: UserSummary,
    val publicationTime: LocalDateTime?,
    val hidingTime: LocalDateTime?,
    val delayedPublicationTime: LocalDateTime?,
    val delayedHidingTime: LocalDateTime?,
    val text: String,
    var viewed: MutableState<Int>,
    val audienceSize: Int,
    val attachments: List<AttachmentContentBase> = emptyList(),
    var seen: Boolean = false
) {
    fun containsClosableSurveys(): Boolean =
        getClosableSurveyIds().any()

    fun getClosableSurveyIds(): List<Uuid> =
        attachments
            .filter {
                it is AttachedSurveyContent && // вложение является опросом
                it.isOpen // и опрос открыт
            }
            .map { (it as AttachedSurveyContent).id }
}