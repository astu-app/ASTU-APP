package org.astu.feature.bulletinBoard.models.dataSoruces.api.attachments.surveys.responses

enum class VoteInSurveyErrors {
    // 403
    /** Пользователь не имеет права голосовать в опросе */
    VotingForbidden,
    // 404
    /** Опрос не существует */
    SurveyDoesNotExist,
    // 409
    /** Опрос закрыт */
    SurveyClosed,
    /** Голос в опросе уже отдан */
    SurveyAlreadyVoted,
    /** В вопросе с единственным выбором нельзя выбрать несколько вариантов ответов */
    CannotSelectMultipleAnswersInSingleChoiceQuestion,
    /** Список представленных вопросов не соответствует фактическому списку вопросов опроса */
    PresentedQuestionsDoesntMatchSurveyQuestions,
    /** Список выбранных вариантов ответов для одного или нескольких вопросов не включается в список вариантов ответов вопроса */
    PresentedVotesDoesntMatchQuestionAnswers,
}