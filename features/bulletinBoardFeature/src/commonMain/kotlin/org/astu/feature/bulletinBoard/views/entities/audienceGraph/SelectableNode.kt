package org.astu.feature.bulletinBoard.views.entities.audienceGraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * Узел графа, который может быть выбоан
 * @param content отрисовываемый контент текущего узла
 */
class SelectableNode(
    override val children: List<ISelectableNode>,
    override val isSelected: MutableState<Boolean> = mutableStateOf(false),
    content: @Composable () -> Unit,
) : Node(children, content), ISelectableNode {
    override val parentNodes: MutableList<ISelectableNode> = mutableListOf()

    override fun setSelectionState(state: Boolean) {
        isSelected.value = state
        children.forEach { it.setSelectionState(state) }
    }

    override fun receiveNotificationChildSelectionChanges() {
        isSelected.value = areAllChildrenSelected()
    }

    private fun areAllChildrenSelected(): Boolean {
        return children.all { it.isSelected.value }
    }
}