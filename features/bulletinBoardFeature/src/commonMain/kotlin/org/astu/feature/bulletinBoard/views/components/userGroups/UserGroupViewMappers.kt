package org.astu.feature.bulletinBoard.views.components.userGroups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupSummaryContent
import kotlin.jvm.JvmName

object UserGroupViewMappers {
    @JvmName("UserGroupSummaryContentToView")
    fun UserGroupSummaryContent.toView(onClick: () -> Unit): @Composable () -> Unit = {
        Text(
            text = this.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(onClick = onClick),
        )
    }
}