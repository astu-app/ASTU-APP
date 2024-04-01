package org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph

import androidx.compose.runtime.MutableState

interface ISelectableNode : INode {
    val isSelected: MutableState<Boolean>

    fun setSelectionState(state: Boolean)
}