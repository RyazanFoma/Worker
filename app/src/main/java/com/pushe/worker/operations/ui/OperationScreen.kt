package com.pushe.worker.operation.ui.summary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.pushe.worker.operations.data.Result
import com.pushe.worker.operations.data.OperationDataSource
import com.pushe.worker.operations.theme.ComposeTheme

@Composable
fun OperationScreen(userId: String, barcode: String) {

    lateinit var operationDataSource: OperationDataSource
    operationDataSource = OperationDataSource(LocalContext.current)
    operationDataSource.requestOperation(barcode)

    ResultScreen(operationDataSource)
}

@Composable
private fun ResultScreen(operationDataSource: OperationDataSource) = ComposeTheme {

    val result by operationDataSource.observeAsState()

        ShowOperationResult(result = result as Result<Any>?)
}
