package org.astu.app.components.bulletinBoard.attachments.surveys.questions.models

import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.AnswerContentBase

abstract class QuestionContentBase(
    val text: String,
    val answers: List<AnswerContentBase>,
)

