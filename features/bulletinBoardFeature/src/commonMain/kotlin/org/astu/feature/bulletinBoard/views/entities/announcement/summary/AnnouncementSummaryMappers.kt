package org.astu.feature.bulletinBoard.views.entities.announcement.summary

import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementSummary
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
            viewed = this.viewed,
            audienceSize = this.audienceSize,
            attachments = mapAttachments(this.surveys, showVoters = false)
        )

    @JvmName("AnnouncementSummaryCollectionToPresentations")
    fun Collection<AnnouncementSummary>.toPresentations(): List<AnnouncementSummaryContent> =
        this.map { it.toPresentation() }
}

