package org.astu.app.entities.bulletInBoard.audienceGraph

import androidx.compose.runtime.Composable

open class Node(
    open val nodes: List<INode>,
    override var content: @Composable () -> Unit
) : INode