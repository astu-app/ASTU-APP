package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses

import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CreateSurveyErrors

/**
 * Агрегат, содержащий набор ошибок, которые могут возникнуть при редактировании объявления
 */
data class EditAnnouncementErrorsAggregate(
    val editAnnouncementError: EditAnnouncementErrors? = null,
    val createSurveyError: CreateSurveyErrors? = null,
)
