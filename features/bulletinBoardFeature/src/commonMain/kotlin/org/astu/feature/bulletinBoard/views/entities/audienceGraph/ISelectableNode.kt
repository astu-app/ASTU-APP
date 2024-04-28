package org.astu.feature.bulletinBoard.views.entities.audienceGraph

import androidx.compose.runtime.MutableState

interface ISelectableNode : INode {
    val isSelected: MutableState<Boolean>

    fun setSelectionState(state: Boolean)
}