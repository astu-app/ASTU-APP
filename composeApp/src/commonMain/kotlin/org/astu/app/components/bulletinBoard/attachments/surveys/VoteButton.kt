package org.astu.app.components.bulletinBoard.attachments.surveys

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun VoteButton(
    text: String = "Голосовать",
    modifier: Modifier = Modifier
        .wrapContentHeight(align = Alignment.CenterVertically)
        .fillMaxWidth()
) {
    Button(
        onClick = { },
        modifier = modifier
    ) {
        Text(text)
    }
}