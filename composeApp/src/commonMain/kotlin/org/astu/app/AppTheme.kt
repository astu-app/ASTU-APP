package org.astu.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import org.astu.infrastructure.theme.*

internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()
    val isDarkState = remember { mutableStateOf(systemIsDark) }
    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkState
    ) {
        val isDark by isDarkState
        SystemAppearance(!isDark)
        currentColorScheme.value = if (isDark) DarkColorScheme else LightColorScheme
        MaterialTheme(
            colorScheme = currentColorScheme.value,
            typography = AppTypography,
            shapes = AppShapes,
            content = {
                Surface(content = content)
            }
        )
    }
}

@Composable
internal expect fun SystemAppearance(isDark: Boolean)