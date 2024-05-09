package org.astu.feature.bulletinBoard.models.dataSoruces

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.models.dataSoruces.api.common.responses.ContentWithError
import org.astu.feature.bulletinBoard.models.dataSoruces.api.surveys.responses.CloseSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.surveys.responses.CreateSurveyErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.surveys.responses.DownloadSurveyResultsErrors
import org.astu.feature.bulletinBoard.models.dataSoruces.api.surveys.responses.VoteInSurveyErrors
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.creation.CreateSurvey
import org.astu.feature.bulletinBoard.models.entities.attachments.survey.voting.VoteInSurvey

interface SurveyDataSource {
    /**
     * Отправить запрос на создание опроса
     */
    suspend fun create(survey: CreateSurvey): ContentWithError<Uuid, CreateSurveyErrors>

    /**
     * Отправить опрос на голосование в опросе
     */
    suspend fun vote(votes: VoteInSurvey): VoteInSurveyErrors?

    /**
     * Отправить запрос на закрытие опроса
     * @param id идентфиикатор закрываемого опроса
     */
    suspend fun close(id: Uuid): CloseSurveyErrors?

    /**
     * Отправить запрос на скачивание результатов запроса
     * @param id идентфиикатор опроса, результаты которого нужно скачать
     */
    suspend fun downloadResults(id: Uuid): DownloadSurveyResultsErrors?
}