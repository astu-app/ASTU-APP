package org.astu.app.components.bulletinBoard.common.models

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.benasher44.uuid.uuid4
import org.astu.app.components.CheckboxRow
import org.astu.app.components.bulletinBoard.announcements.common.SelectableUserSummary

@Deprecated("Class is deprecated and shouldn't be user anymore")
object UserStorage {
    val user1 = UserSummary(uuid4(), "Анисимова", "Анна", "Максимовна")
    val user2 = UserSummary(uuid4(), "Романова", "Милана", "Матвеевна")
    val user3 = UserSummary(uuid4(), "Фадеев", "Максим", "Михайлович")
    val user4 = UserSummary(uuid4(), "Покровская", "Анастасия", "Андреевна")
    val user5 = UserSummary(uuid4(), "Воробьева", "Софья", "Дмитриевна")
    val user6 = UserSummary(uuid4(), "Левин", "Тимофей", "Владимирович")

    fun makeStaticUserText(user: UserSummary, modifier: Modifier = Modifier): @Composable () -> Unit {
        return makeStaticUserText(user.firstName, user.secondName, user.patronymic, modifier)
    }

    fun makeStaticUserText(firstName: String, secondName: String, patronymic: String?, modifier: Modifier = Modifier): @Composable () -> Unit {
        return {
            Column(modifier = modifier) {
                Text(
                    text = firstName
                )
                val secondPartOfName =
                    if (patronymic != null)
                        "$secondName $patronymic"
                    else secondName
                Text(
                    text = secondPartOfName,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }

    fun makeSelectableUserText(user: SelectableUserSummary, modifier: Modifier = Modifier, onCheckedStateChanged: (Boolean) -> Unit): @Composable () -> Unit {
        return {
            CheckboxRow(
                title = {
                    Column(modifier = modifier) {
                        Text(
                            text = user.firstName
                        )
                        val secondPartOfName =
                            if (user.patronymic != null)
                                "${user.secondName} ${user.patronymic}"
                            else user.secondName
                        Text(
                            text = secondPartOfName,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                },
                state = user.isSelected,
                onCheckedStateChange = onCheckedStateChanged
            )
        }
    }
}