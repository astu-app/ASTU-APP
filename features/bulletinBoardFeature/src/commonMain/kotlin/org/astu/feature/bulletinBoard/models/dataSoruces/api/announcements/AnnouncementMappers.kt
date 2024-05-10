package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements

import com.benasher44.uuid.uuidFrom
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.ContentForAnnouncementUpdatingDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.CreateAnnouncementDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.dtos.UpdateAnnouncementDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.files.FileMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.SurveyToModelMappers.toModels
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.dtos.UpdateIdentifierListDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.mappers.UpdateIdentifierListMappers.toDto
import org.astu.feature.bulletinBoard.models.dataSoruces.api.userGroups.UserGroupMappers.toModel
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.feature.bulletinBoard.models.entities.announcements.EditAnnouncement
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

    /**
     * @param attachmentIds идентфиикаторы вложений в фармате uuid
     */
    fun CreateAnnouncement.toDto(attachmentIds: List<String>): CreateAnnouncementDto =
        CreateAnnouncementDto(
            content = this.content,
            userIds = this.userIds,
            attachmentIds = attachmentIds,
            delayedPublishingAt = this.delayedPublishingAt,
            delayedHidingAt = this.delayedHidingAt,
            categoryIds = this.categoryIds,
        )

    fun EditAnnouncement.toDto(newAttachmentIds: List<String>?): UpdateAnnouncementDto =
        UpdateAnnouncementDto(
            id = this.id.toString(),
            content = this.content,
            categoryIds = this.categories.toDto(),
            audienceIds = this.users.toDto(),
            attachmentIds = UpdateIdentifierListDto(
                toAdd = newAttachmentIds?.toSet() ?: emptySet(),
                toRemove = this.attachmentIdsToRemove?.map { it.toString() }?.toSet() ?: emptySet()
            ),
            delayedPublishingAtChanged = this.delayedPublishingAtChanged,
            delayedPublishingAt = this.delayedPublishingAt,
            delayedHidingAtChanged = this.delayedHidingAtChanged,
            delayedHidingAt = this.delayedHidingAt,
        )
}