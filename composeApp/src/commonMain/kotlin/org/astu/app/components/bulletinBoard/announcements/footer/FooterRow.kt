package org.astu.app.components.bulletinBoard.announcements.footer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.astu.app.theme.CurrentColorScheme

@Composable
fun FooterRow(text: String, modifier: Modifier = Modifier.fillMaxWidth()) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = CurrentColorScheme?.outline ?: Color.Gray,
            textAlign = TextAlign.End,
            lineHeight = 1.33.em,
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium),
            modifier = Modifier
                .requiredWidth(width = 185.dp)
                .wrapContentHeight(align = Alignment.CenterVertically))
    }
}