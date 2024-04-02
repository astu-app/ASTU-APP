package org.astu.app.entities.bulletInBoard.audienceGraph

import androidx.compose.runtime.MutableState

interface ISelectableNode : INode {
    val isSelected: MutableState<Boolean>

    fun setSelectionState(state: Boolean)
}