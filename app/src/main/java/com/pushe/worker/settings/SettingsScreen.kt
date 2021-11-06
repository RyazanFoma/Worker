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
import androidx.compose.ui.unit.dp
import com.pushe.worker.settings.data.AccountPreferences
import com.pushe.worker.settings.model.SettingsViewModel

@ExperimentalComposeUiApi
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val scaffoldState = rememberScaffoldState()
    val accountFlow = viewModel.flow.collectAsState(initial = AccountPreferences())
    val backgroundColor = MaterialTheme.colors.primary

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
                PreferenceField(
                    label = "Путь",
                    preference = accountFlow.value.path,
                    description = "URL-адрес для обращения к http-сервису 1С:ERP",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    backgroundColor = backgroundColor,
                    onValue = viewModel::path,
                )
                PreferenceField(
                    label = "Пользователь",
                    preference = accountFlow.value.account,
                    description = "Имя пользователя для аунтефикации на веб-сервере 1С:ERP",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                    backgroundColor = backgroundColor,
                    onValue = viewModel::account,
                )
                PreferenceField(
                    label = "Пароль",
                    preference = accountFlow.value.password,
                    description = "Пароль пользователя для аунтефикации на веб-сервере 1С:ERP",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    backgroundColor = backgroundColor,
                    onValue = viewModel::password,
                )
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
    keyboardOptions: KeyboardOptions,
    backgroundColor: Color,
    onValue: (newValue: String) -> Unit,
) {
    var value by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    var passwordVisibility by remember { mutableStateOf(false) }
    var trailingIcon: @Composable (() -> Unit)? =  null

    value = preference

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
        value = value,
        onValueChange = { value = it; onValue(it) },
        singleLine = true,
        keyboardActions = KeyboardActions { keyboardController?.hide() },
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        visualTransformation = if (passwordVisibility ||
            KeyboardType.Password != keyboardOptions.keyboardType)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        colors = textFieldColorsMono(backgroundColor = backgroundColor)
    )
    Divider(modifier = Modifier.padding(top = 16.dp))
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
