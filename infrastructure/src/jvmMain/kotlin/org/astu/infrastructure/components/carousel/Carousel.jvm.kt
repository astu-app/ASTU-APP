package org.astu.infrastructure.components.carousel

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun Carousel(modifier: Modifier, content: @Composable () -> Unit) {
    val state = rememberScrollState()
    Column {
        Row(modifier.horizontalScroll(state)) {
            content()
        }
        HorizontalScrollbar(adapter = rememberScrollbarAdapter(state))
    }
}