package org.astu.feature.bulletinBoard.views.components.userGroups.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.benasher44.uuid.Uuid
import org.astu.feature.bulletinBoard.views.entities.users.UserSummary
import org.astu.infrastructure.components.dropdown.DropDown

@Composable
fun SelectAdminSection(
    admin: UserSummary?,
    adminCandidates: Map<Uuid, @Composable () -> Unit>,
    adminPresentation: @Composable () -> Unit,
    resetAdmin: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        if (admin == null) {
            // Если админ не выбран, показываем элемент для выбора
            DropDown(
                adminCandidates.values,
            ) {
                Text("Администратор")
            }

        } else {
            // Если же админ выбран, то отображаем его краткие данные

            Text("Администратор")
            Row(verticalAlignment = Alignment.CenterVertically) {
                adminPresentation.invoke()

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = "Удалить администратора",
                        modifier = Modifier.clickable { resetAdmin.invoke() }
                    )
                }
            }
        }
    }
}