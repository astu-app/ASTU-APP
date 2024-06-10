package org.astu.feature.bulletinBoard.views.entities.attachments.creation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.astu.feature.bulletinBoard.views.entities.ContentProvider
import org.astu.feature.bulletinBoard.views.entities.DefaultModifierProvider
import org.astu.infrastructure.components.SwitchRow
import org.astu.infrastructure.components.common.getButtonColors
import org.astu.infrastructure.theme.CurrentColorScheme

/**
 * Класс для создания вопроса
 */
class NewQuestion : ContentProvider, DefaultModifierProvider {
    var isMultipleChoiceAllowed: MutableState<Boolean> = mutableStateOf(false)
    var content: MutableState<String> = mutableStateOf("")

    var answers: SnapshotStateMap<Int, NewAnswer> = mutableStateMapOf()
    private var lastAnswerId: Int = 0

    var onQuestionDeleteRequest: () -> Unit = { }



    init {
        val id = lastAnswerId
        answers[id] = NewAnswer { answers.remove(id) }
        lastAnswerId++
    }



    fun isValid(): Boolean {
        return content.value.isNotBlank()
                && answers.count() > 1
                && answers.all { (_, answer) -> answer.isValid() }
    }

    @Composable
    override fun Content(modifier: Modifier) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CurrentColorScheme.secondaryContainer),
            modifier = modifier,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = CurrentColorScheme.outlineVariant
                )

                OutlinedTextField(
                    value = content.value,
                    onValueChange = { content.value = it },
                    label = { Text("Вопрос") },
                    trailingIcon = {
                        Icon(
                            Icons.Outlined.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable { onQuestionDeleteRequest() }
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    answers.forEach { (_, answer) -> answer.Content(answer.getDefaultModifier()) }

                    ElevatedButton(
                        onClick = {
                            val id = lastAnswerId
                            answers[id] = NewAnswer{ answers.remove(id) }
                            lastAnswerId++
                        },
                        colors = Color.getButtonColors(
                            containerColor = CurrentColorScheme.secondaryContainer
                        ),
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(top = 8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Icon(Icons.Outlined.Add, null)
                            Text("Добавить ответ")
                        }
                    }
                }

                SwitchRow("Выбор нескольких ответов", isMultipleChoiceAllowed)

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = CurrentColorScheme.outlineVariant
                )
            }
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    }
}