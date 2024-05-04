package org.astu.feature.bulletinBoard.views.components.attachments.voting.questions.models

import org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models.AnswerContentBase
import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider

abstract class QuestionContentBase(
    val text: String,
    val answers: List<AnswerContentBase>
) : DefaultModifierProvider, ContentProvider

