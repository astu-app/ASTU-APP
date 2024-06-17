package org.astu.infrastructure.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.astu.infrastructure.modifierUtils.ignoreHorizontalParentPadding
import org.astu.infrastructure.theme.CurrentColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
    dialogHorizontalExpansion: Dp = 0.dp,
    modifier: Modifier = Modifier
        .clip(RoundedCornerShape(32.dp))
        .background(CurrentColorScheme.surface),
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.ignoreHorizontalParentPadding(dialogHorizontalExpansion),
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            items(1) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = CurrentColorScheme.outline
                    )
                }

                content()

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(end = 12.dp, bottom = 12.dp),
                ) {
                    OutlinedButton(
                        modifier = Modifier.padding(end = 4.dp),
                        onClick = { onDismissRequest() }
                    ) {
                        Text("Отменить")
                    }
                    OutlinedButton(
                        modifier = Modifier.padding(start = 4.dp),
                        onClick = { onConfirmRequest() }
                    ) {
                        Text("ОК")
                    }
                }
            }
        }
    }
}


