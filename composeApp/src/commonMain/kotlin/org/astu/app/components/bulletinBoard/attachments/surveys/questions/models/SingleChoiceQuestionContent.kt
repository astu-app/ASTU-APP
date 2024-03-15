package org.astu.app.components.bulletinBoard.attachments.surveys.questions.models

import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.SingleChoiceAnswerContent

class SingleChoiceQuestionContent (
    text: String,
    answers: List<SingleChoiceAnswerContent>
) : QuestionContentBase(text, answers)