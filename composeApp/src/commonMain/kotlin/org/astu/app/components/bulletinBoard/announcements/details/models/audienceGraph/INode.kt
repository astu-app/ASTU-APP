package org.astu.app.components.bulletinBoard.announcements.details.models.audienceGraph

import androidx.compose.runtime.Composable

interface INode {
    var content: @Composable () -> Unit
}