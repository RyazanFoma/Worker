package com.pushe.worker.operation.ui.summary

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OperationText(text: String) {

    Text(
        text = text,
        Modifier
            .padding(all = 20.dp)
            .fillMaxWidth(),
        style = MaterialTheme.typography.h5
    )

    Spacer(modifier = Modifier.height(4.dp))
}