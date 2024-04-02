package org.astu.app.entities.bulletInBoard.audienceGraph

import androidx.compose.runtime.Composable

open class Leaf(override var content: @Composable () -> Unit) : INode