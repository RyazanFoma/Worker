package com.pushe.worker.utils

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Show error message
 * @param error message string
 * @param onRefresh lambda to start updating data
 * @param onBack lambda to back if onRefresh is null
 * @param timeOut time to exit back if onRefresh is null
 */
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalAnimationApi
@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    error: String,
    onRefresh: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    timeOut: Long = 10_000L,
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
            contentColor = MaterialTheme.colors.onError,
            backgroundColor = MaterialTheme.colors.error,
            actionOnNewLine = true,
            action = onRefresh?.let {
                {
                    Button(
                        onClick = {
                            it()
                            visible.targetState = false
                        }
                    )
                    { Text(text = "Повторить") }
                }
            }
        ) { Text(text = "ОШИБКА: $error") }
    }
    if (onRefresh == null) {
        scope.launch {
            delay(timeOut)
            visible.targetState = false
            onBack?.let {
                delay(1_000L)
                it()
            }
        }
    }
}
