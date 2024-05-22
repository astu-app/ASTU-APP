package org.astu.feature.bulletinBoard.views.components.userGroups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.entities.userGroups.UserGroupSummaryContent
import kotlin.jvm.JvmName

object UserGroupViewMappers {
    @JvmName("UserGroupSummaryContentToView")
    fun UserGroupSummaryContent.toView(onClick: () -> Unit): @Composable () -> Unit = {
        val groupName = remember { this.name }
        val adminName = remember { this.adminName }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                text = groupName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable(onClick = onClick),
            )
            Text(
                text = adminName ?: "Без администратора",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )
        }
    }
}