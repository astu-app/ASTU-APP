package org.astu.app.components.bulletinBoard.announcements.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import org.astu.app.theme.CurrentColorScheme

@Composable
fun ViewersCount(
    viewed: Int,
    audienceSize: Int,
    viewedPercent: Int? = null,
    color: Color = CurrentColorScheme.outline,
    textAlign: TextAlign = TextAlign.End,
    style: TextStyle = MaterialTheme.typography.bodySmall,
    modifier: Modifier = Modifier
) {
    val text = if (viewedPercent != null) "$viewed / $audienceSize, $viewedPercent%" else "$viewed / $audienceSize"
    Text(
        text = text,
        color = color,
        textAlign = textAlign,
        style = style,
        modifier = modifier
    )
}