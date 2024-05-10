package org.astu.feature.bulletinBoard.models.services.surveys

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.SurveyDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.ApiSurveyDataSource
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CloseSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.CreateSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses.VoteInSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateSurvey
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.voting.VoteInSurvey

class SurveyService {
    private val source: SurveyDataSource = ApiSurveyDataSource()

    suspend fun create(survey: CreateSurvey): ContentWithError<Uuid, CreateSurveyErrors> =
        source.create(survey)

    suspend fun vote(votes: VoteInSurvey): VoteInSurveyErrors? =
        source.vote(votes)

    suspend fun close(id: Uuid): CloseSurveyErrors? =
        source.close(id)
}