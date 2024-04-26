package org.astu.app.components.bulletinBoard.attachments.surveys.answers.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class MultipleChoiceAnswerContent(
    text: String,
    var selected: MutableState<Boolean> = mutableStateOf(false)
) : AnswerContentBase(text)