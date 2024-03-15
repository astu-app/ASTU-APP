package org.astu.app.components.bulletinBoard.attachments.surveys.questions.models

import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.MultipleChoiceAnswerContent

class MultipleChoiceQuestionContent(
    text: String,
    answers: List<MultipleChoiceAnswerContent>
) : QuestionContentBase(text, answers)