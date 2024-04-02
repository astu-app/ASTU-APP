package org.astu.app.entities.bulletInBoard.audienceGraph

import androidx.compose.runtime.Composable

interface INode {
    var content: @Composable () -> Unit
}