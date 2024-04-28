package org.astu.feature.bulletinBoard.views.entities.audienceGraph

import androidx.compose.runtime.Composable

open class Node(
    open val nodes: List<INode>,
    override var content: @Composable () -> Unit
) : INode