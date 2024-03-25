package org.astu.infrastructure.components.card

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Composable
fun Title(title: String, fontWeight: FontWeight? = FontWeight.Bold, fontSize: TextUnit = TextUnit.Unspecified){
    Text(title, fontWeight = fontWeight, fontSize = fontSize)
}