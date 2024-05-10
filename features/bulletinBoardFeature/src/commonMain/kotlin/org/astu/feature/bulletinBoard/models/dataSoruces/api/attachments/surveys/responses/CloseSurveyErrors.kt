package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses

enum class CloseSurveyErrors {
    // 403
    /** Пользователь не имеет права закрыть этот опрос */
    SurveyClosingForbidden,
    // 404
    /** Опрос не существует */
    SurveyDoesNotExist,
    // 409
    /** Опрос уже закрыт */
    SurveyAlreadyClosed,
}