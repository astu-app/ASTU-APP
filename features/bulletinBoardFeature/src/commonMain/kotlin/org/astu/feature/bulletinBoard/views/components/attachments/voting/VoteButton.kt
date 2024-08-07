package org.astu.feature.bulletinBoard.views.components.attachments.voting

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun VoteButton(
    buttonContent: @Composable () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
        .wrapContentHeight(align = Alignment.CenterVertically)
        .fillMaxWidth(),
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        buttonContent()
    }
}