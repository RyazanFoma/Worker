package com.pushe.worker.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.pushe.worker.settings.model.SettingsViewModel

@ExperimentalComposeUiApi
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val scaffoldState = rememberScaffoldState()
    var path by rememberSaveable { mutableStateOf("") }
    var account by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val backgroundColor = MaterialTheme.colors.primary

    path = viewModel.preferences.path
    account = viewModel.preferences.account
    password = viewModel.preferences.password

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Доступ к 1C:ERP") },
            )
        },
        backgroundColor = backgroundColor,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Start,

            ) {
                Text(
                    text = "URL-адрес для обращения к http-сервису 1С:ERP",
                    modifier = Modifier.padding(top = 16.dp)
                )
                TextField(
                    label = { Text("Путь") },
                    value = path,
                    onValueChange = { path = it; viewModel.path(it) },
                    singleLine = true,
                    keyboardActions = KeyboardActions { keyboardController?.hide() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    colors = textFieldColorsMono(backgroundColor = backgroundColor)
                )
                Divider(modifier = Modifier.padding(top = 16.dp))

                Text(
                    text = "Логин пользователя для обращения к http-сервису 1С:ERP",
                    modifier = Modifier.padding(top = 16.dp)
                )
                TextField(
                    label = { Text("Пользователь") },
                    value = account,
                    onValueChange = { account = it; viewModel.account(it) },
                    singleLine = true,
                    keyboardActions = KeyboardActions { keyboardController?.hide() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                    colors = textFieldColorsMono(backgroundColor = backgroundColor)
                )
                Divider(modifier = Modifier.padding(top = 16.dp))

                Text(
                    text = "Пароль пользователя для аунтефикации на веб-сервере 1С:ERP",
                    modifier = Modifier.padding(top = 16.dp)
                )
                TextField(
                    label = { Text("Пароль") },
                    value = password,
                    onValueChange = { password = it; viewModel.password(it) },
                    singleLine = true,
                    keyboardActions = KeyboardActions { keyboardController?.hide() },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            Icon(
                                imageVector = if (passwordVisibility) Icons.Filled.Visibility
                                else Icons.Filled.VisibilityOff, ""
                            )
                        }
                    },
                    visualTransformation = if (passwordVisibility)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    colors = textFieldColorsMono(backgroundColor = backgroundColor)
                )
                Divider(modifier = Modifier.padding(top = 16.dp))
            }
        }
    )
}

@Composable
fun textFieldColorsMono(
    backgroundColor: Color,
    color: Color = contentColorFor(backgroundColor = backgroundColor)
) = TextFieldDefaults.textFieldColors(
    textColor = color,
    disabledTextColor = color,
    backgroundColor = backgroundColor,
    cursorColor = color,
    errorCursorColor = color,
    focusedIndicatorColor = color,
    unfocusedIndicatorColor = color,
    disabledIndicatorColor = color,
    errorIndicatorColor = color,
    leadingIconColor = color,
    disabledLeadingIconColor = color,
    errorLeadingIconColor = color,
    trailingIconColor = color,
    disabledTrailingIconColor = color,
    errorTrailingIconColor = color,
    focusedLabelColor = color,
    unfocusedLabelColor = color,
    disabledLabelColor = color,
    errorLabelColor = color,
    placeholderColor = color,
    disabledPlaceholderColor = color
)
