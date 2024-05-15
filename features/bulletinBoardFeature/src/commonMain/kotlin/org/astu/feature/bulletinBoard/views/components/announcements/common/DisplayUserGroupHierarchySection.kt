package org.astu.feature.bulletinBoard.views.components.announcements.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.components.tree.Tree
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.INode

@Composable
fun DisplayUserGroupHierarchySection(
    rootNodes: List<INode>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(all = 16.dp)
        .wrapContentHeight()
) {
    Column(modifier = modifier) {
        Tree(rootNodes)
    }
}