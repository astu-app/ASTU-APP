package org.astu.app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.model.rememberScreenModel
import org.astu.app.view_models.AuthViewModel
import org.astu.infrastructure.SerializableScreen

class AuthScreen(private val onAuth: () -> Unit) : SerializableScreen {
    private lateinit var viewModel: AuthViewModel

    @Composable
    override fun Content() {
        viewModel = rememberScreenModel { AuthViewModel() }
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            if (maxWidth < 700.dp)
                mobileView()
            else
                desktopView()
        }
    }

    @Composable
    fun desktopView() {
        Row(horizontalArrangement = Arrangement.Center) {
            authPanel(Modifier.fillMaxHeight().padding(horizontal = 40.dp))
//            HorizontalDivider(Modifier.weight(0.01f).fillMaxHeight(), 4.dp)
//            authDetail(Modifier.weight(6f))
        }
    }

    @Composable
    fun mobileView() {
        Row {
            authPanel(Modifier.fillMaxSize())
        }
    }

    @Composable
    private fun authPanel(modifier: Modifier) {
        var email by remember { viewModel.email }
        var password by remember { viewModel.password }
        val error by remember { viewModel.error }
        var passwordVisibility by remember { mutableStateOf(false) }

        Column(
            modifier = modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Авторизация",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Логин", fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Пароль", fontSize = 12.sp) },
                singleLine = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        val imageVector =
                            if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility
                        Icon(
                            imageVector,
                            contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                        )
                    }
                }
            )
            Button(
                onClick = { viewModel.login(onAuth) },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Войти")
            }
            if(error != null)
                Text(error!!,modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Button({viewModel.loginAsStudent(onAuth)}, colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Yellow, contentColor = Color.Black)){
                Text("Войти как студент")
            }
            Button({viewModel.loginAsEmployee(onAuth)}, colors = ButtonDefaults.buttonColors().copy(containerColor = Color.Yellow, contentColor = Color.Black)){
                Text("Войти как сотрудник")
            }
        }
    }

    @Composable
    private fun authDetail(modifier: Modifier) {
        Column(modifier) {
//            Text("Здесь был текст")
            Image(Icons.Default.Call, null, modifier = Modifier.fillMaxSize())
        }
    }
}