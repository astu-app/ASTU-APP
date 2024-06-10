package org.astu.feature.bulletinBoard.views.components.attachments.voting.answers.models

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

    val nextId: Int
        get() = lastId + 1



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