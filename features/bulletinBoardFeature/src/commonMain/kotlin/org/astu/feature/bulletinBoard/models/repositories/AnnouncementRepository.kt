package org.astu.feature.bulletinBoard.models.repositories

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.ApiGeneralAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.ApiPublishedAnnouncementDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.CreateAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.EditAnnouncementErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetAnnouncementDetailsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses.GetPostedAnnouncementListErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.surveys.ApiSurveyDataSource
import org.astu.feature.bulletinBoard.models.entities.announcements.AnnouncementDetails
import org.astu.feature.bulletinBoard.models.entities.announcements.ContentForAnnouncementEditing
import org.astu.feature.bulletinBoard.models.entities.announcements.CreateAnnouncement
import org.astu.feature.bulletinBoard.models.entities.announcements.EditAnnouncement
import org.astu.feature.bulletinBoard.views.entities.announcement.summary.AnnouncementSummaryContent
import org.astu.feature.bulletinBoard.views.entities.attachments.AttachmentMappers.mapAttachments

class AnnouncementRepository {
    private val publishedAnnouncementsSource = ApiPublishedAnnouncementDataSource()
    private val generalAnnouncementsSource = ApiGeneralAnnouncementDataSource()
    private val surveySource = ApiSurveyDataSource()

    suspend fun loadList(): ContentWithError<List<AnnouncementSummaryContent>, GetPostedAnnouncementListErrors> {
        val content = publishedAnnouncementsSource.getList()
        val announcements = content.content?.map {
            AnnouncementSummaryContent(
                id = it.id,
                author = it.author,
                publicationTime = it.publicationTime,
                text = it.text,
                viewed = it.viewed,
                audienceSize = it.audienceSize,
                attachments = mapAttachments(it.files, it.surveys)
            )
        }

        return ContentWithError(announcements, content.error)
    }

    suspend fun loadDetails(id: Uuid): ContentWithError<AnnouncementDetails, GetAnnouncementDetailsErrors> {
        return publishedAnnouncementsSource.getDetails(id)
    }

    suspend fun create(announcement: CreateAnnouncement): CreateAnnouncementErrors? {
        return generalAnnouncementsSource.create(announcement)
    }

    suspend fun loadEditContent(id: Uuid): ContentWithError<ContentForAnnouncementEditing, EditAnnouncementErrors> {
        return generalAnnouncementsSource.getUpdateForAnnouncementEditing(id)
    }

    suspend fun edit(announcement: EditAnnouncement): EditAnnouncementErrors? {
        return generalAnnouncementsSource.edit(announcement)
    }
}