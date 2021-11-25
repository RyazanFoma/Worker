package com.pushe.worker.operations.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pushe.worker.operations.model.OperationViewModel
import com.pushe.worker.utils.ScanScreen

@ExperimentalMaterialApi
@Composable
fun OperationScan(
    userId: String,
    viewModel: OperationViewModel,
    onBack: () -> Unit,
    ) {
    var barCode by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        ScanScreen(
            modifier = Modifier.fillMaxSize(),
            statusText = "Штрих код операции",
        ) { barCode = it }
    }
    if (barCode.isNotBlank()) {
        OperationScreen(
            userId = userId,
            barCode = barCode,
            status = viewModel.status,
            operation = viewModel.operation,
            error = viewModel.error,
            onRefresh = viewModel::load,
            onCompleted = viewModel::completed,
            onBack = onBack,
        )
    }
}