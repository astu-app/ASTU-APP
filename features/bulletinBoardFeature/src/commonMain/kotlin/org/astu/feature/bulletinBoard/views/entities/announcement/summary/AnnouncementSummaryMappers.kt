package org.astu.feature.bulletinBoard.views.entities.announcement.summary

import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementSummary
import org.astu.feature.bulletinBoard.views.entities.attachments.AttachmentToPresentationMappers.mapAttachments
import kotlin.jvm.JvmName

object AnnouncementSummaryMappers {
    @JvmName("AnnouncementSummaryToPresentation")
    fun AnnouncementSummary.toPresentation(): AnnouncementSummaryContent =
        AnnouncementSummaryContent(
            this.id,
            this.author,
            this.publicationTime,
            this.text,
            this.viewed,
            this.audienceSize,
            mapAttachments(this.files, this.surveys)
        )

    @JvmName("AnnouncementSummaryCollectionToPresentations")
    fun Collection<AnnouncementSummary>.toPresentations(): List<AnnouncementSummaryContent> =
        this.map { it.toPresentation() }
}

