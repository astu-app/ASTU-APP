package org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph

import androidx.compose.runtime.Composable

open class Leaf(override var content: @Composable () -> Unit) : INode {
    override val parentNodes: MutableList<INode> = mutableListOf()
}