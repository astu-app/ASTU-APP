package org.astu.feature.bulletinBoard.views.entities.audienceGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class SelectableNode(
    override val nodes: List<ISelectableNode>,
    override val isSelected: MutableState<Boolean> = mutableStateOf(false),
    content: @Composable () -> Unit,
) : Node(nodes, content), ISelectableNode {
    override fun setSelectionState(state: Boolean) {
        isSelected.value = state
        nodes.forEach { it.setSelectionState(state) }
    }
}