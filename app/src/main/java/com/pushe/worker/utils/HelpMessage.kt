package com.pushe.worker.utils

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ScreenRotation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pushe.worker.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class MESSAGE {SWIPE_DOWN, SWIPE_ROTATION}

@ExperimentalMaterialApi
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun HelpMessage(
    modifier: Modifier = Modifier,
    message: MESSAGE,
    timeOut: Long = 5_000L,
) {
    val visible  = remember { MutableTransitionState(false)
        .apply { targetState = true }
    }
    val scope = rememberCoroutineScope()

    AnimatedVisibility(
        visibleState = visible,
        enter = fadeIn(),
        exit = fadeOut(),
    ) {
        Snackbar(
            modifier = modifier
                .padding(50.dp)
                .widthIn(max = 400.dp),
            contentColor = MaterialTheme.colors.onSecondary,
            backgroundColor = MaterialTheme.colors.secondary,
            actionOnNewLine = true,
            action = {
                Button(onClick = { visible.targetState = false }) { Text(text = "OK") }
            }
        ) {
            Column(modifier = modifier) {
                when (message) {
                    MESSAGE.SWIPE_DOWN ->
                        ListItem(
                            modifier = modifier,
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_swipe_down_black_24dp),
                                    contentDescription = "Потянуть вниз",
                                    modifier = Modifier.size(48.dp)
                                )
                            },
                        ) { Text( "Потяните вниз для обновления") }
                    MESSAGE.SWIPE_ROTATION -> {
                        ListItem(
                            modifier = modifier,
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.ScreenRotation,
                                    contentDescription = "Повернуть экран",
                                    modifier = Modifier.size(48.dp)
                                )
                            },
                        ) { Text( "Поверните экран для анализа") }
                        ListItem(
                            modifier = modifier,
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_swipe_right_black_24dp),
                                    contentDescription = "Смахнуть вправо",
                                    modifier = Modifier.size(48.dp)
                                )
                            },
                        ) { Text( "Смахните влево на следующий период") }
                        ListItem(
                            modifier = modifier,
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_swipe_left_black_24dp),
                                    contentDescription = "Смахнуть влево",
                                    modifier = Modifier.size(48.dp)
                                )
                            },
                        ) { Text( "Вправо на предыдущий период") }
                    }
                }
            }
        }
    }
    scope.launch {
        delay(timeOut)
        visible.targetState = false
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Preview
@Composable
fun Preview0() {
    HelpMessage(message = MESSAGE.SWIPE_ROTATION)
}