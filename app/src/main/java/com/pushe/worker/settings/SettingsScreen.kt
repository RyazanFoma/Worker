package com.pushe.worker.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@ExperimentalComposeUiApi
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val scaffoldState = rememberScaffoldState()
//    var path: String by rememberSaveable { mutableStateOf("init") }

    var path: String by rememberSaveable { mutableStateOf("") }
    var account: String by rememberSaveable { mutableStateOf("") }
    var password: String by rememberSaveable { mutableStateOf("") }

    path = viewModel.path
    account = viewModel.account
    password =viewModel.password

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Доступ к 1C:ERP") },
            )
        },
        backgroundColor = MaterialTheme.colors.surface,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                PreferenceField(
                    label = "Путь",
                    preference = path,
                    description = "Путь к интерфейсу 1С:ERP в http-формате",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                ) { viewModel.path = it; path = it }
                PreferenceField(
                    label = "Пользователь",
                    preference = account,
                    description = "Имя пользователя для подключения к 1С:ERP",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                ) { viewModel.account = it; account = it }
                PreferenceField(
                    label = "Пароль",
                    preference = password,
                    description = "Пароль пользователя для подключения",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                ) { viewModel.password = it; password = it }
            }
        }
    )
}

@ExperimentalComposeUiApi
@Composable
private fun PreferenceField(
    label: String,
    preference: String,
    description: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions,
    onValue: (newValue: String) -> Unit,
) {
//    var value by rememberSaveable { mutableStateOf(preference) }
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }
    var trailingIcon: @Composable (() -> Unit)? =  null

    if (KeyboardType.Password == keyboardOptions.keyboardType) {
        trailingIcon = {
            val image = if (passwordVisibility) Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = image, "")
            }
        }
    }

    Text(text = description, modifier = Modifier.padding(top = 16.dp))
    TextField(
        label = { Text(label) },
        value = preference,
        onValueChange = { /*value = it;*/ onValue(it) },
        singleLine = true,
        keyboardActions = KeyboardActions { keyboardController?.hide() },
        modifier = modifier.padding(0.dp),
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        visualTransformation = if (passwordVisibility ||
            KeyboardType.Password != keyboardOptions.keyboardType)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),

    )
    Divider(modifier = Modifier.padding(top = 16.dp))
}
