package org.astu.feature.bulletinBoard.models.dataSoruces.api.announcements.responses

import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CreateSurveyErrors

/**
 * Агрегат, содержащий набор ошибок, которые могут возникнуть при создании объявления
 */
data class CreateAnnouncementErrorsAggregate(
    val createAnnouncementError: CreateAnnouncementErrors? = null,
    val createSurveyError: CreateSurveyErrors? = null,
)
