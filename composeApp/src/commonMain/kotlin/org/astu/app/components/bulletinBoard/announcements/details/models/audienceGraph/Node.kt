package org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph

import androidx.compose.runtime.Composable

open class Node(
    open val nodes: List<INode>,
    override var content: @Composable () -> Unit
) : INode