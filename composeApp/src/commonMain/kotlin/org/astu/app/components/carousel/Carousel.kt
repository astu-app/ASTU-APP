package org.astu.app.components.carousel

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Carousel(modifier: Modifier, content: @Composable () -> Unit)