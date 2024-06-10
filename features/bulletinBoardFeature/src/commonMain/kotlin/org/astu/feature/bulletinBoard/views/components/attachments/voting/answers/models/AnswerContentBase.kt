package org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models

import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider

abstract class AnswerContentBase(val id: Uuid, val text: String) : ContentProvider, DefaultModifierProvider
