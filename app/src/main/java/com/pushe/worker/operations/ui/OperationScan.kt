package com.pushe.worker.operations.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import com.pushe.worker.operations.model.OperationViewModel
import com.pushe.worker.utils.ScanScreen

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun OperationScan(
    userId: String,
    viewModel: OperationViewModel,
    onBack: () -> Unit,
    ) {
    var barCode by rememberSaveable { mutableStateOf("") }
    var offsetX by rememberSaveable { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        ScanScreen(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { offsetX = it.width / 2  },
            statusText = "Штрих код операции",
            backgroundMode = barCode.isNotBlank(),
        ) { barCode = it }
        if (barCode.isNotBlank()) {
            OperationScreen(
                userId = userId,
                barCode = barCode,
                viewModel = viewModel,
                offsetX = offsetX,
            ) {
                barCode = ""
                onBack()
            }
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun OperationScreen(
    userId: String,
    barCode: String?,
    viewModel: OperationViewModel,
    offsetX: Int,
    onBack: () -> Unit,
) {
    var direction by remember { mutableStateOf(LEFT) }
    var visible by remember { mutableStateOf(true) }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(initialOffsetX = { offsetX }),
        exit = slideOutHorizontally(targetOffsetX = { direction * offsetX }),
    ) {
        OperationScreen(
            userId = userId,
            barCode = barCode,
            status = viewModel.status,
            operation = viewModel.operation,
            error = viewModel.error,
            onRefresh = viewModel::load,
            onCompleted = { number, userId ->
                direction = RIGHT
                visible = false
                viewModel.completed(number = number, userId = userId)
                          } ,
            onBack = {
                direction = LEFT
                visible = false
                onBack()
                     },
        )
    }
}

private const val LEFT = -1
private const val RIGHT = 1