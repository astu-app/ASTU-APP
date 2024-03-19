package org.astu.app.components.bulletinBoard.announcements.creation.models

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import org.astu.app.components.bulletinBoard.common.models.ContentProvider
import org.astu.app.components.bulletinBoard.common.models.DefaultModifierProvider

/**
 * Класс для создания варианта ответа
 */
class NewAnswer(private var onAnswerDeleteRequest: () -> Unit, ) : ContentProvider, DefaultModifierProvider {
    val content: MutableState<String> = mutableStateOf("")



    fun isValid(): Boolean {
        return content.value.isNotBlank()
    }

    @Composable
    override fun Content(modifier: Modifier) {
        Column(modifier = modifier) {
            OutlinedTextField(
                value = content.value,
                onValueChange = { content.value = it },
                label = { Text("Вариант ответа") },
                trailingIcon = {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = null,
                        modifier = Modifier.clickable { onAnswerDeleteRequest() }
                    )
                },
                modifier = modifier
            )
        }
    }

    override fun getDefaultModifier(): Modifier {
        return Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    }
}