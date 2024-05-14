package org.astu.feature.bulletinBoard.views.components.tree

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.entities.audienceGraph.INode

@Composable
fun Tree(
    rootNodes: List<INode>,
    levelIndent: Dp = 16.dp,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 8.dp)
        // offset используется для того, чтобы первый элемент дерева был на одном уровне с заголовком дерева
        .offset(x = -levelIndent),
) {
    Column(modifier = modifier) {
        rootNodes.forEach { node -> PlaceThreeNode(node, levelIndent) }
    }
}