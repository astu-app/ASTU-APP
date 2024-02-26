package org.astu.app.components.card

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit

@Composable
fun Description(title: String, fontWeight: FontWeight? = null, fontSize: TextUnit = TextUnit.Unspecified){
    Text(title, fontWeight = fontWeight, fontSize = fontSize, maxLines = 2, overflow = TextOverflow.Ellipsis)
}