package org.astu.app.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen

class AuthScreen(private val onAuth: () -> Unit)  : Screen {
    @Composable
    override fun Content() {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            if (maxWidth < 700.dp)
                mobileView()
            else
                desktopView()
        }
    }

    @Composable
    fun desktopView() {
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            authPanel(Modifier.weight(2f).fillMaxHeight())
            Divider(Modifier.weight(0.01f).fillMaxHeight(), 4.dp)
            authDetail(Modifier.weight(6f))
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
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
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
                label = { Text("Email", fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Password", fontSize = 12.sp) },
                singleLine = true,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        val imageVector =
                            if (passwordVisibility) Icons.Default.Close else Icons.Default.Edit
                        Icon(
                            imageVector,
                            contentDescription = if (passwordVisibility) "Hide password" else "Show password"
                        )
                    }
                }
            )
            Button(
                onClick = onAuth,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Login")
            }
        }
    }

    @Composable
    private fun authDetail(modifier: Modifier) {
        Column(modifier) {
            Text("Здесь был текст")
            Image(Icons.Default.Call, null, modifier = Modifier.fillMaxSize())
        }
    }
}