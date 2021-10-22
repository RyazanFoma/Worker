package com.pushe.worker.logup.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.pushe.worker.R

@ExperimentalAnimationApi
@Composable
fun LogUp(
    userName: String,
    onClickButton: () -> Unit,
    onLogIn: () -> Unit,
    onLogOut: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    var size by remember { mutableStateOf(Size.Zero) }
    val density = LocalDensity.current
    val visibilityLogo = remember(size) { with(density) { size.height.toDp() } > 400.dp }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text("Worker.1C:ERP") },
                actions = {
                    IconButton(
                        onClick = { scope.launch { scaffoldState.drawerState.open() } }
                    ) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Setting"
                        )
                    }
                }
            )
        },
        backgroundColor = MaterialTheme.colors.primary,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .onSizeChanged { size = Size(it.width.toFloat(), it.height.toFloat()) },
                horizontalAlignment = CenterHorizontally
            ) {
                if (visibilityLogo) Logo(name = "PUSHE®", image = R.drawable.pushe_logo)
                else Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colors.primary,
                ) {
                    var visibleLogIn by remember { mutableStateOf(false) }
                    var direction by remember { mutableStateOf(0) }
                    BarCodeButton {
                        visibleLogIn = true
                        onClickButton()
                    }
                    AnimatedVisibility(
                        visible = visibleLogIn,
                        enter = slideInHorizontally(
                            initialOffsetX = { LEFT * size.width.toInt() / 2 }
                        ),
                        exit = slideOutHorizontally(
                            targetOffsetX = { direction * size.width.toInt() / 2 }
                        )
                    ) {
                        LogIn(
                            login = userName,
                            onPasswordChange = { true },
                            onLogIn = {
                                visibleLogIn = false
                                direction = RIGHT
                                onLogIn()
                            },
                            onLogOut = {
                                visibleLogIn = false
                                direction = LEFT
                                onLogOut()
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
@Suppress("SameParameterValue")
private fun Logo(name: String, image: Int) {
    Column(horizontalAlignment = CenterHorizontally) {
        Icon(
            painter = painterResource(id = image),
            tint= Color.Unspecified,
            contentDescription = name,
            modifier = Modifier
                .size(200.dp)
                .padding(top = 50.dp)
        )
        Text(text = name)
    }
}

@Composable
private fun BarCodeButton(onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(150.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_barcode_color),
                tint= Color.Unspecified,
                contentDescription = "Barcode",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp)
            )
        }
        Text(text = "Нажмите для входа...")
    }
}

private const val LEFT = -1
private const val RIGHT = 1