package org.astu.app.components.bulletinBoard.announcements.footer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AnnouncementFooter(viewed: Int, audienceSize: Int, modifier: Modifier = Modifier.fillMaxWidth()) {
    Column(
        modifier = modifier
    ) {
        FooterRow("$viewed / $audienceSize")
    }
}