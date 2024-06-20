package org.astu.feature.single_window.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.astu.feature.single_window.client.models.RequestDTO
import org.astu.feature.single_window.view_models.UserCreatedRequestViewModel
import org.astu.feature.single_window.view_models.UserRequestViewModel
import org.astu.infrastructure.AccountUser
import org.astu.infrastructure.DependencyInjection.GlobalDIContext
import org.astu.infrastructure.JavaSerializable

class UserRequestScreen(
    private val vm: UserRequestViewModel,
    onReturn: (() -> Unit)?,
    onChange: (ServiceScreen) -> Unit
) : ServiceScreen("Просмотр заявления", onReturn, onChange), JavaSerializable {
    private lateinit var viewModel: UserCreatedRequestViewModel

    @Composable
    override fun Content() {
        viewModel = remember { UserCreatedRequestViewModel(vm.currentRequest.value!!) }

        val request = remember { viewModel.request }

        Column(Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                text = request.name,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp
            )
            HorizontalDivider()
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 6.dp, bottom = 6.dp, start = 8.dp, end = 8.dp),
                text = request.description,
                lineHeight = 26.sp,
                fontSize = 20.sp
            )
            HorizontalDivider(thickness = 3.dp)
            if (request.fields.isNotEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                    text = "Заполненные требования",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                HorizontalDivider()
                Column {
                    request.fields.forEach { prop ->
                        Column(Modifier.padding(horizontal = 15.dp, vertical = 5.dp)) {
                            Text("Название требования: ${prop.name}")
                            Text("Описание требования: ${prop.description}")
                        }
                        Row {
                            Text(
                                "Значение: ${prop.value as String}",
                                modifier = Modifier.fillMaxWidth().padding(start = 15.dp, top = 5.dp)
                            )
                        }

                    }
                }
                HorizontalDivider(thickness = 3.dp)
            }
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                text = "Тип заявки",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            HorizontalDivider(thickness = 3.dp)
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = if (request.type == RequestDTO.Type.FaceToFace) "Очное получение" else "По почте"
            )
            HorizontalDivider(thickness = 3.dp)
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                text = "Статус",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
            HorizontalDivider(thickness = 3.dp)
            Text(modifier = Modifier.fillMaxWidth(), text = request.status.string())
            HorizontalDivider(thickness = 3.dp)
            request.message?.let {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
                    text = "Комментарий",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                )
                HorizontalDivider(thickness = 3.dp)

                Text(modifier = Modifier.fillMaxWidth(), text = it)
            }
            viewModel.error.value?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            if(request.status == RequestDTO.Status.InProgress)
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = viewModel::remove) {
                    Text("Отозвать заявление")
                }
            }
        }
    }

    private fun RequestDTO.Status.string(): String {
        return when (this) {
            RequestDTO.Status.Success -> "Справка готова"
            RequestDTO.Status.InProgress -> "Справка изготавливается"
            RequestDTO.Status.Denied -> "В получении отказано"
            RequestDTO.Status.Removed -> "Вы отменили заявление"
        }
    }
}