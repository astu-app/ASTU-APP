package org.astu.feature.bulletinBoard.views.entities.audienceGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

class SelectableLeaf(
    override val isSelected: MutableState<Boolean>,
    content: @Composable () -> Unit,
) : Leaf(content), ISelectableNode {
    override val parentNodes: MutableList<ISelectableNode> = mutableListOf()

    override fun setSelectionState(state: Boolean) {
        isSelected.value = state
    }

    /**
     * Метод ничего не делает
     */
    override fun receiveNotificationChildSelectionChanges() {
    }
}