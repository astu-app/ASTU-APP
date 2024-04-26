package org.astu.infrastructure.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun ComboBox(
    modifier: Modifier,
    expanded: Boolean,
    onAccept: () -> Unit,
    onDismiss: () -> Unit,
    text: @Composable RowScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    BoxWithConstraints(modifier) {
        TextButton(
            onAccept,
            Modifier.width(maxWidth),
            border = BorderStroke(1.dp, Color.Black),
            shape = RectangleShape,
            content = text
        )
        DropdownMenu(expanded, onDismiss, Modifier.width(maxWidth), content = content)
    }
}
