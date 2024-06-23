package org.astu.infrastructure.components.dropdown

import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.astu.infrastructure.theme.CurrentColorScheme

@Composable
fun DropDown(
    items: Collection<@Composable () -> Unit>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 8.dp),
    isExpanded: MutableState<Boolean>,
    title: @Composable () -> Unit,
) {
    var currentRotation by remember { mutableStateOf(0f) }
    val rotation = remember { Animatable(currentRotation) }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable { isExpanded.value = !isExpanded.value }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,) {
                Row(modifier = Modifier.weight(1f),) {
                    title()
                }

                LaunchedEffect(isExpanded.value) {
                    val targetRotation = if (isExpanded.value) currentRotation - 180f else currentRotation + 180f
                    rotation.animateTo(targetRotation, animationSpec = tween(300)) {
                        currentRotation = value
                    }
                }
                Icon(
                    Icons.Default.ArrowDropUp,
                    contentDescription = null,
                    modifier = Modifier
                        .requiredSize(width = 24.dp, height = 24.dp)
                        .weight(0.1f)
                        .rotate(currentRotation)
                )
            }
        }

        // анимация развертывания и сворачивания
        val density = LocalDensity.current
        AnimatedVisibility(
            visible = isExpanded.value,
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
            exit = slideOutVertically() + shrinkVertically() + fadeOut()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
            ) {
                items.forEach {
                    DropDownMenuItem(it)
                }
            }
        }
    }
}

@Composable
private fun DropDownMenuItem(
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