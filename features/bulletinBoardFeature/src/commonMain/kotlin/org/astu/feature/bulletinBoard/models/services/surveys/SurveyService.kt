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
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.GatewayServiceConfig

class SurveyService {
    private val config by GlobalDIContext.inject<GatewayServiceConfig>()
    private val source: SurveyDataSource = ApiSurveyDataSource(config.url)

    suspend fun create(survey: CreateSurvey, rootUserGroupId: Uuid): ContentWithError<Uuid, CreateSurveyErrors> =
        source.create(survey, rootUserGroupId)

    suspend fun vote(votes: VoteInSurvey): VoteInSurveyErrors? =
        source.vote(votes)

    suspend fun close(id: Uuid): CloseSurveyErrors? =
        source.close(id)
}