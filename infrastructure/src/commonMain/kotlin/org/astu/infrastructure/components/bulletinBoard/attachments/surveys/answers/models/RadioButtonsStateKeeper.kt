package org.astu.app.components.bulletinBoard.attachments.surveys.answers.models

import androidx.compose.runtime.mutableStateOf

class RadioButtonsStateKeeper {
    var selectedStateId: Int
        get() {
            return selectedId.value
        }
        set(value) {
            if (value> lastId) {
                return
            }

            selectedId.value = value
        }



    private var lastId = 0
    private var selectedId = mutableStateOf(-1)
    private val states = mutableMapOf<Int, SingleChoiceAnswerContent>()



    fun add(state: SingleChoiceAnswerContent): Int {
        lastId++
        states[lastId] = state;
        return lastId;
    }

    fun getSelected() : Int {
        return selectedId.value
    }

    fun getState(id: Int): SingleChoiceAnswerContent? {
        return states[id]
    }

    fun getIds(): List<Int> {
        return states.keys.toList()
    }
}