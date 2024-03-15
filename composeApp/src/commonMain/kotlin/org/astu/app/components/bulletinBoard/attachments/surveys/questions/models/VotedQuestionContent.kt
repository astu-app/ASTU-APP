package org.astu.app.components.bulletinBoard.attachments.surveys.questions.models

import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.VotedAnswerContent

class VotedQuestionContent(
    text: String,
    answers: List<VotedAnswerContent>
) : QuestionContentBase(text, answers)