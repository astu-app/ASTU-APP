package org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models

import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider

abstract class AnswerContentBase(val text: String) : ContentProvider, DefaultModifierProvider
