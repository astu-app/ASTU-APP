package org.astu.feature.bulletinBoard.views.components.tree

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.INode
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.Node
import org.astu.infrastructure.theme.CurrentColorScheme

@Composable
fun PlaceThreeNode(
    node: INode,
    indent: Dp,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(start = indent)
    ) {
        TreeNodeContent(node.content)
        if (node is Node) {
            node.children.forEach { PlaceThreeNode(it, indent) }
        }
    }
}

@Composable
private fun TreeNodeContent(
    content: @Composable (() -> Unit)?,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)
) {
    if (content == null)
        return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            content()
        }
    }

    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth(),
        color = CurrentColorScheme.outline
    )
}
