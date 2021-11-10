package com.pushe.worker.operations.ui.summary

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.pushe.worker.data.Result
import com.pushe.worker.operations.model.OperationDataSource
import com.pushe.worker.operations.ui.theme.ui.theme.ComposeTheme

@Composable
fun SummaryScreen(userId: String, barcode: String) {

    lateinit var operationDataSource: OperationDataSource;
    operationDataSource = OperationDataSource(LocalContext.current)
    operationDataSource.requestOperation(barcode)

    ResultScreen(operationDataSource)
}

@Composable
private fun ResultScreen(operationDataSource: OperationDataSource) = ComposeTheme {

    val result by operationDataSource.observeAsState()

        ShowOperationResult(result = result as Result<Any>?)
}
