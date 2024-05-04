package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.ContentForAnnouncementUpdatingDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.files.FileMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.AttachmentMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.UserGroupMappers.toModel
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import kotlin.jvm.JvmName

object AnnouncementMappers {
    @JvmName("ContentForAnnouncementUpdatingDtoToModel")
    fun ContentForAnnouncementUpdatingDto.toModel(): ContentForAnnouncementEditing =
        ContentForAnnouncementEditing(
            id = uuidFrom(this.id),
            authorName = this.authorName,
            content = this.content,
            viewsCount = this.viewsCount,
            audienceSize = this.audienceSize,
            audienceHierarchy = this.audienceHierarchy.toModel(),
            files = this.files.toModels(),
            surveys = this.surveys.toModels(),
            publishedAt = this.publishedAt,
            hiddenAt = this.hiddenAt,
            delayedHidingAt = this.delayedHidingAt,
            delayedPublishingAt = this.delayedPublishingAt,
        )
}