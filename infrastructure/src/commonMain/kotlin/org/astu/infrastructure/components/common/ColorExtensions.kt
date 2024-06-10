package org.astu.infrastructure.components.common

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun Color.Companion.getButtonColors(
    containerColor: Color,
    contentColor: Color = contentColorFor(containerColor)
): ButtonColors {
    return ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor
    )
}

@Composable
inline fun Color.Companion.getCardColors(
    containerColor: Color,
    contentColor: Color = contentColorFor(containerColor)
): CardColors {
    return CardDefaults.cardColors(
        containerColor = containerColor,
        contentColor = contentColor
    )
}

