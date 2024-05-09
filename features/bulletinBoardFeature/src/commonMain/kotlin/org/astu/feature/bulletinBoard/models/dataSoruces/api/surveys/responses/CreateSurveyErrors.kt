package org.astu.feature.bulletinBoard.models.dataSoruces.api.surveys.responses

enum class CreateSurveyErrors {
    // 403
    /** Пользователь не имеет права создать опрос */
    CreateSurveyForbidden,
    // 409
    /** Опрос содержит вопросы с одинаковыми порядковыми номерами */
    SurveyContainsQuestionSerialsDuplicates,
    /** Вопрос содержит варианты ответов с повторяющимися порядковыми номерами */
    QuestionContainsAnswersSerialsDuplicates,
}