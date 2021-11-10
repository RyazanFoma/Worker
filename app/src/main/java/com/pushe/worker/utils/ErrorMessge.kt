package com.pushe.worker.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorMessage(error: String, onRefresh: (() -> Unit)? = null) {
    Snackbar(
        modifier = Modifier.padding(50.dp),
        contentColor = MaterialTheme.colors.onError,
        backgroundColor = MaterialTheme.colors.error,
        actionOnNewLine = true,
        action = onRefresh?.let { { Button(onClick = it) { Text(text = "Retry") } } }
    ) { Text(text = "ОШИБКА: " + error) }
}
