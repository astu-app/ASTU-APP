package org.astu.feature.bulletinBoard.models

import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.VoteInSurveyErrors
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.voting.VoteInSurvey
import org.astu.feature.bulletinBoard.models.services.surveys.SurveyService

class SurveyModel {
    private val surveyService: SurveyService = SurveyService()

    suspend fun vote(votes: VoteInSurvey): VoteInSurveyErrors? =
        surveyService.vote(votes)
}