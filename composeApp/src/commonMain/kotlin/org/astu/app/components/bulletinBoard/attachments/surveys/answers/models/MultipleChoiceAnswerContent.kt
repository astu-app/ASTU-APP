package org.astu.app.components.bulletinBoard.attachments.surveys.answers.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class MultipleChoiceAnswerContent(
    override val text: String,
    var selected: MutableState<Boolean> = mutableStateOf(false)
) : AnswerContentBase()