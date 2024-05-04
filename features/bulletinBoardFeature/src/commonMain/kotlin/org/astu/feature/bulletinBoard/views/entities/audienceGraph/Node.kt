package org.astu.feature.bulletinBoard.views.entities.audienceGraph

import androidx.compose.runtime.Composable

/**
 * Узел графа
 * @param children список дочерних узлов текущего узла
 * @param content отрисовываемый контент текущего узла
 */
open class Node(
    open val children: List<INode>,
    override var content: @Composable () -> Unit
) : INode