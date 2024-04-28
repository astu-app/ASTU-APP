package org.astu.feature.bulletinBoard.views.components.attachments.surveys.questions.models

import org.astu.feature.bulletinBoard.views.components.attachments.surveys.answers.models.AnswerContentBase
import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider

abstract class QuestionContentBase(val text: String, val answers: List<AnswerContentBase>)
    : DefaultModifierProvider, ContentProvider

