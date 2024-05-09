package org.astu.feature.bulletinBoard.models.dataSoruces.api.surveys.responses

enum class DownloadSurveyResultsErrors {
    // 400
    /** Тип файла с результатами опроса не поддерживается */
    FileTypeNotSupported,
    // 403
    /** Пользователь не имеет права выгрузить результаты этого опроса */
    SurveyResultsDownloadingForbidden,
    // 404
    /** Опрос не существует */
    SurveyDoesNotExist,
    // 409
    /** Объявление с опросом еще не опубликовано */
    AnnouncementWithSurveyNotYetPublished,
}