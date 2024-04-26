package org.astu.infrastructure.components.carousel

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Carousel(modifier: Modifier = Modifier, content: @Composable ()-> Unit)

