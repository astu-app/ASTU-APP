package org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

class SelectableLeaf(
    content: @Composable () -> Unit,
    override val isSelected: MutableState<Boolean>,
) : Leaf(content), ISelectableNode {
    override fun setSelectionState(state: Boolean) {
        isSelected.value = state
    }
}