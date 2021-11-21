package com.pushe.worker.operation.ui.summary

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pushe.worker.operations.data.Result
import com.pushe.worker.operations.data.Operation
import com.pushe.worker.utils.BarCode

@Composable
fun ShowOperationResult(result: Result<Any>?) {

    var operation = Operation(name = "---", date = "---", amount = 0f, sum = 0f)

    if (result is Result.Success<*>) {
        operation = result.data as Operation
    }

    Column(
        Modifier.verticalScroll(
            state = ScrollState(0),
            enabled = true
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            elevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                OperationText(text = "Операция: ${operation.name}")
                OperationText(text = "Дата: ${operation.date}")
                OperationText(text = "Количество: ${operation.amount}")
                OperationText(text = "Сумма: ${operation.sum}")
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        ) {
            ResultForm(result)
        }
    }
}