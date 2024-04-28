package org.astu.feature.bulletinBoard.views.entities.audienceGraph

import androidx.compose.runtime.Composable

interface INode {
    var content: @Composable () -> Unit
}