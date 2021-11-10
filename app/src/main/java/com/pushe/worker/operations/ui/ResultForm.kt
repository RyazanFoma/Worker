package com.pushe.worker.operation.ui.summary

import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pushe.worker.R
import com.pushe.worker.operations.data.Result
import com.pushe.worker.operations.theme.Blue200
import com.pushe.worker.operations.theme.Purple200
import com.pushe.worker.operations.theme.Purple350

@Composable
fun ResultForm(result: Result<Any>?) {

    when (result) {
        null -> {
            CircularProgressIndicator(
                color = colorResource(id = R.color.purple_200),
                strokeWidth = Dp(value = 4F),
                modifier = Modifier.padding(40.dp)
            )
        }
        is Result.Success -> {
            ResultText(
                text = stringResource(R.string.done_result_text),
                colors = listOf(
                    Blue200,
                    Purple350
                ),
                borderColor = Purple200
            )
        }
        is Result.Error -> {
            ResultText(
                text = stringResource(R.string.done_result_text),
                colors = listOf(
                    Color.LightGray,
                    Color.LightGray
                ),
                borderColor = Color.Gray
            )
        }
    }
}