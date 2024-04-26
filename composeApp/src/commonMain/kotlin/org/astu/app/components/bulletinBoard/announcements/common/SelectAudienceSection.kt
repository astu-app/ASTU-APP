package org.astu.app.components.bulletinBoard.announcements.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.app.components.dropdown.TreeDropDown
import org.astu.app.entities.bulletInBoard.audienceGraph.INode
import org.astu.app.theme.CurrentColorScheme

@Composable
fun SelectAudienceSection(
    rootNodes: List<INode>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(all = 16.dp)) {
            TreeDropDown(
                rootNodes = rootNodes,
                title = { Text("Аудитория") }
            )
        }
    }
}