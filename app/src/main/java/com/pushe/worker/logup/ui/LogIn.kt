package com.pushe.worker.logup.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.textFieldColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pushe.worker.theme.WorkerTheme

@Composable
fun LogIn(
    login: String,
    onPasswordChange: (String) -> Boolean,
    onLogIn: () -> Unit,
    onLogOut: () -> Unit
) {
    var password: String by rememberSaveable { mutableStateOf("") }
    val backgroundColor = MaterialTheme.colors.primaryVariant.copy(0.6f)
    var enabledLogIn: Boolean by remember { mutableStateOf(false) }

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
            val textFieldColors = textFieldColorsLogin(
                backgroundColor = backgroundColor,
                color = MaterialTheme.colors.secondary
            )

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
                colors = textFieldColors
            )
            Row {
                var passwordVisibility by remember { mutableStateOf(false) }
                OutlinedTextField(
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password"
                        )
                    },
                    placeholder = { Text("Password") },
                    value = password,
                    onValueChange = {
                        password = it
                        enabledLogIn = onPasswordChange(password)
                    },
                    visualTransformation = if (passwordVisibility)
                        VisualTransformation.None
                        else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                    colors = textFieldColors
                )
                FloatingActionButton(
                    onClick = { onLogIn() },
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
            FloatingActionButton(
                onClick = { onLogOut() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Logout"
                )
            }
        }
    }
}

@Composable
private fun textFieldColorsLogin(
    backgroundColor: Color,
    color: Color = contentColorFor(backgroundColor = backgroundColor)
) = textFieldColors(
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

@Preview
@Composable
fun DefaultPreview() {
    WorkerTheme {
        LogIn("Иванов Иван Иванович", { true }, {}, {})
    }
}
