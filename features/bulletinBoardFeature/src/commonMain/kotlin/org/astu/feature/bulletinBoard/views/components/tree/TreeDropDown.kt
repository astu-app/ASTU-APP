package org.astu.feature.bulletinBoard.views.components.tree

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.entities.userGroups.audienceGraph.INode

/**
 * Выпадающий список, позволяющий выводить древовидные структуры. Каждый из уровней дерева выделяется отступом от
 * предыдущего
 * @param title Заголовок выпадающего списка
 * @param rootNode корневой узел дерева
 * @param levelIndent Отступ очередного уровня от предыдущего
 * @param modifier Модификатор объекта, применяющийся к основному контейнеру выпадающего списка
 */
@Composable
fun TreeDropDown(
    rootNode: INode,
    levelIndent: Dp = 16.dp,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 8.dp),
    title: @Composable () -> Unit,
) {
    TreeDropDown(listOf(rootNode), levelIndent, modifier, title)
}

@Composable
fun TreeDropDown(
    rootNodes: List<INode>,
    levelIndent: Dp = 16.dp,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 8.dp),
    title: @Composable () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var currentRotation by remember { mutableStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { isExpanded = !isExpanded }
        ) {
            title()
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                    .fillMaxWidth()
            ) {
                LaunchedEffect(isExpanded) {
                    val targetRotation = if (isExpanded) currentRotation - 180f else currentRotation + 180f
                    rotation.animateTo(targetRotation, animationSpec = tween(300)) {
                        currentRotation = value
                    }
                }
                Icon(
                    Icons.Default.ArrowDropUp,
                    contentDescription = null,
                    modifier = Modifier
                        .requiredSize(width = 24.dp, height = 24.dp)
                        .rotate(currentRotation)
                )
            }
        }

        // анимация развертывания и сворачивания
        val density = LocalDensity.current
        AnimatedVisibility(
            visible = isExpanded,
            enter = slideInVertically {
                // Slide in from 40 dp from the top.
                with(density) { -40.dp.roundToPx() }
            } + expandVertically(
                // Expand from the top.
                expandFrom = Alignment.Top
            ) + fadeIn(
                // Fade in with the initial alpha of 0.3f.
                initialAlpha = 0.3f
            ),
            exit = slideOutVertically() + shrinkVertically() + fadeOut(),
            // offset используется для того, чтобы первый элемент дерева был на одном уровне с заголовком дерева
            modifier = Modifier.offset(x = -levelIndent)
        ) {
//            rootNodes.forEach { rootNode -> PlaceThreeNode(rootNode, levelIndent) { content -> DropDownMenuItem(content) } }
            rootNodes.forEach { rootNode -> PlaceThreeNode(rootNode, levelIndent) }
        }
    }
}