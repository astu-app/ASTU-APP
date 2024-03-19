package org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph

import androidx.compose.runtime.Composable

class Node(val nodes: List<NodeBase>, content: @Composable (() -> Unit)?) : NodeBase(content)