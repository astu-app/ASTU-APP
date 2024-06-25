package org.astu.feature.bulletinBoard.views.entities.users

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.astu.infrastructure.components.extendedIcons.material.CheckSmall
import kotlin.jvm.JvmName

object UserToViewMappers {
    @JvmName("CheckableUserCollectionToViews")
    fun Collection<CheckableUserSummary>.toViews(modifier: Modifier = Modifier): List<@Composable () -> Unit> =
        this.map { it.toView(modifier) }

    @JvmName("CheckableUserToView")
    fun CheckableUserSummary.toView(modifier: Modifier = Modifier): @Composable () -> Unit =
    {
        val firstName = remember { this.firstName }
        val secondName = remember { this.secondName }
        val patronymic = remember { this.patronymic }
        val isChecked = remember { this.isChecked }

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f)
            ) {
                Text(firstName)
                val secondPartOfName =
                    if (patronymic != null)
                        "$secondName $patronymic"
                    else secondName
                Text(
                    text = secondPartOfName,
                    style = MaterialTheme.typography.labelMedium,
                )
            }

            if (isChecked) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(all = 8.dp)
                        .weight(0.2f)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CheckSmall,
                        contentDescription = "Просмотрел ли пользователь объявление",
                    )
                }
            }
        }
    }

    @JvmName("UserCollectionToViews")
    fun Collection<UserSummary>.toViews(modifier: Modifier = Modifier): List<@Composable () -> Unit> =
        this.map { it.toView(modifier) }

    @JvmName("UserToView")
    fun UserSummary.toView(modifier: Modifier = Modifier): @Composable () -> Unit =
        {
            val firstName = remember { this.firstName }
            val secondName = remember { this.secondName }
            val patronymic = remember { this.patronymic }

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .weight(1f)
                ) {
                    Text(secondName)
                    val secondPartOfName =
                        if (patronymic != null)
                            "$firstName $patronymic"
                        else firstName
                    Text(
                        text = secondPartOfName,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
}