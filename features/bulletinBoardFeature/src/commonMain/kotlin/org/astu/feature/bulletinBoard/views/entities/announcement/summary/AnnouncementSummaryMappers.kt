package org.astu.feature.bulletinBoard.views.entities.announcement.summary

import androidx.compose.runtime.mutableStateOf
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementSummary
import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.VotedAnswerContentSummary
import org.astu.feature.bulletinBoard.views.entities.attachments.AttachmentToPresentationMappers.mapAttachments
import org.astu.feature.bulletinBoard.views.entities.users.UserToPresentationMappers.toPresentation
import kotlin.jvm.JvmName

object AnnouncementSummaryMappers {
    @JvmName("AnnouncementSummaryToPresentation")
    fun AnnouncementSummary.toPresentation(): AnnouncementSummaryContent =
        AnnouncementSummaryContent(
            id = this.id,
            author = this.author.toPresentation(),
            publicationTime = this.publicationTime,
            hidingTime = this.hidingTime,
            delayedPublicationTime = this.delayedPublicationTime,
            delayedHidingTime = this.delayedHidingTime,
            text = this.text,
            viewed = mutableStateOf(this.viewed),
            audienceSize = this.audienceSize,
            attachments = mapAttachments<VotedAnswerContentSummary>(this.surveys, showVoters = false)
        )

    @JvmName("AnnouncementSummaryCollectionToPresentations")
    fun Collection<AnnouncementSummary>.toPresentations(): List<AnnouncementSummaryContent> =
        this.map { it.toPresentation() }
}

