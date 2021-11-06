package com.pushe.worker.logup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pushe.worker.settings.textFieldColorsMono
import com.pushe.worker.theme.WorkerTheme

@ExperimentalComposeUiApi
@Composable
fun LogIn(
    login: String,
    onLogIn: (password: String) -> Unit,
    onLogOut: () -> Unit,
    isError: Boolean,
) {
    val backgroundColor = MaterialTheme.colors.primaryVariant.copy(0.6f)

    Card(
        modifier = Modifier
            .padding(16.dp)
            .requiredWidth(332.dp),
        backgroundColor = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            val textFieldColors = textFieldColorsMono(
                backgroundColor = backgroundColor,
                color = MaterialTheme.colors.secondary
            )
            Row1(login = login, colors = textFieldColors)
            Row2(onLogIn = onLogIn, colors = textFieldColors)

            Row3(onLogOut = onLogOut, isError = isError)
        }
    }
}

@Composable
private fun Row1(login: String, colors: TextFieldColors) {
    OutlinedTextField(
        modifier = Modifier.requiredWidth(300.dp),
        readOnly = false,
        value = login,
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Login"
            )
        },
        colors = colors
    )
}

@ExperimentalComposeUiApi
@Composable
private fun Row2(onLogIn: (password: String) -> Unit, colors: TextFieldColors) {
    Row {
        var password: String by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        val keyboardController = LocalSoftwareKeyboardController.current

        OutlinedTextField(
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password"
                )
            },
            placeholder = { Text("Password") },
            value = password,
            onValueChange = { password = it },
            visualTransformation = if (passwordVisibility) VisualTransformation.None
                else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions { keyboardController?.hide() },
            singleLine = true,
            trailingIcon = {
                val image = if (passwordVisibility) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisibility = !passwordVisibility}) {
                    Icon(imageVector  = image, "")
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .requiredWidth(228.dp),
            colors = colors
        )
        FloatingActionButton(
            onClick = { onLogIn(password) },
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp)
                .requiredWidth(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Login"
            )
        }
    }
}

@Composable
private fun Row3(onLogOut: () -> Unit, isError: Boolean) {
    Row {
        FloatingActionButton(
            onClick = { onLogOut() },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Logout",
            )
        }
        if (isError)  {
            Snackbar(
                modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 20.dp),
                backgroundColor = MaterialTheme.colors.error.copy(0.6f),
                contentColor = MaterialTheme.colors.onError,
                shape = MaterialTheme.shapes.medium,
            ) {
                Text(
                    text = "Неверный пароль!",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

}

@ExperimentalComposeUiApi
@Preview
@Composable
fun DefaultPreview() {
    WorkerTheme {
        LogIn("Иванов Иван Иванович", {}, {}, true)
    }
}
