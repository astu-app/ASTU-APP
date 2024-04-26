package org.astu.app.components.bulletinBoard.attachments.surveys.questions.models

import org.astu.app.components.bulletinBoard.attachments.surveys.answers.models.AnswerContentBase
import org.astu.app.components.bulletinBoard.common.models.ContentProvider
import org.astu.app.components.bulletinBoard.common.models.DefaultModifierProvider

abstract class QuestionContentBase(val text: String, val answers: List<AnswerContentBase>)
    : DefaultModifierProvider, ContentProvider

