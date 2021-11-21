package com.pushe.worker.operations.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import com.pushe.worker.operations.data.Operation
import com.pushe.worker.utils.BarCode
import com.pushe.worker.utils.Chip
import com.pushe.worker.utils.ErrorMessage
import com.pushe.worker.utils.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class Own {MY, OTHER, UNKNOWN, NULL}

@ExperimentalMaterialApi
@Composable
fun OperationScreen(
    userId: String,
    barCode: String?,
    status: Status,
    operation: Operation,
    error: String,
    onRefresh: (barCode: String) -> Unit,
    onCompleted: (number: String, userId: String) -> Unit,
    onBack: () -> Unit,
    ) {
    val scope = rememberCoroutineScope()

    Column() {
        Heading(onBack = onBack, barCode = barCode)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Center
        ) {
            Middle(
                modifier = Modifier.placeholder(
                    visible = operation.number == null,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = Color.LightGray.copy(alpha = 0.2f)
                ),
                operation = operation
            )
            when (status) {
                Status.UNKNOWN ->
                    barCode?.let(onRefresh)
                Status.LOADING ->
                     CircularProgressIndicator(
                         Modifier
                             .size(50.dp)
                             .padding(top = 75.dp),
                        color = MaterialTheme.colors.secondary
                    )
                Status.ERROR ->
                    ErrorMessage(
                        error = error,
                        onRefresh = { barCode?.let(onRefresh) }
                    )
                else -> {}
            }
        }
        Footer(
            userId = userId,
            operation = operation,
            onCompleted = onCompleted,
            onBack = onBack,
            scope = scope,
        )
    }
}

@Composable
private fun Heading(onBack: () -> Unit, barCode: String?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        FloatingActionButton(
            modifier = Modifier.padding(8.dp),
            onClick = onBack
        ) { Icon(Icons.Filled.ArrowBack, contentDescription = null) }
        barCode?.let {
            Surface(modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(8.dp),
                shape = RoundedCornerShape(25),
                color = Color.LightGray
            ) {
                BarCode(barCode = barCode)
            }
        }
    }
    Divider()
}

@ExperimentalMaterialApi
@Composable
private fun Middle(modifier: Modifier = Modifier, operation: Operation) {
    ListItem(
        modifier = modifier,
        icon = {
            Icon(
                imageVector = Icons.Rounded.TaskAlt,
                contentDescription = "Выполнено",
                modifier = modifier.size(48.dp)
            )
        },
        overlineText = { Text(text = operation.info1()) },
        text = { Text(text = operation.info2()) },
        secondaryText = { Text(text = operation.info3()) },
        singleLineSecondaryText = false,
    )
}

@Composable
private fun Footer(
    userId: String,
    operation: Operation,
    onCompleted: (number: String, userId: String) -> Unit,
    onBack: () -> Unit,
    scope: CoroutineScope,
) {
    var own: Own by rememberSaveable { mutableStateOf(Own.UNKNOWN) }

    own = when {
        operation.number == null -> Own.NULL
        operation.worker == null -> Own.UNKNOWN
        userId == operation.worker -> Own.MY
        else -> Own.OTHER
    }
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        contentAlignment = CenterEnd
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
        ) {
            Chip(
                text = "Моя",
                selected = (own == Own.MY)
            ) {
                Icon(
                    modifier = it,
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            }
            Chip(
                modifier = Modifier.padding(start = 8.dp),
                text = "Чужая",
                selected = (own == Own.OTHER)
            ) {
                Icon(
                    modifier = it,
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null
                )
            }
        }
        ExtendedFloatingActionButton(
            backgroundColor = when(own) {
                Own.UNKNOWN -> MaterialTheme.colors.secondary
                else -> Color.LightGray
            },
            contentColor = MaterialTheme.colors.onSecondary,
            icon = { Icon(Icons.Filled.Done, contentDescription = "Работа выполнена") },
            text = { Text("ВЫПОЛНЕНО") },
            onClick = {
                if (own == Own.UNKNOWN) {
                    operation.number?.let {
                        onCompleted(it, userId)
                        operation.worker = userId
                        scope.launch {
                            delay(3000)
                            onBack()
                        }
                    }
                }
            }
        )
    }
}

//@ExperimentalMaterialApi
//@Preview
//@Composable
//fun Preview() {
//    OperationScreen(
//        userId = "0001",
//        barCode = "C764",
//        status = Status.LOADING,
//        operation = Operation(
//            number = "007",
//            name = "Раскрой",
//            type = "Раскрой чехла дивана 140",
//            amount = 1f,
//            unit = "шт",
//            date = "2021-11-18T15:38:41",
//            worker = "0001",
//        ),
//        error = "Error message",
//        onRefresh = {},
//        onCompleted = {_,_ -> },
//        onBack = {}
//    )
//}

private fun String?.convertDate() : String {
    this?.let{if (it.length > 9) {
        return "${it.substring(8, 10)}.${it.substring(5, 7)}.${it.substring(0, 4)}"}
    }
    throw IllegalArgumentException("The input argument does not contain a date string yyyy-mm-dd")
}

private fun Operation.info1() : String {
    val datetime = date?.let {
        if (it.length > 15) it.convertDate() + " " + it.substring(11, 16) + " - "
        else null } ?: ""
    return "${ datetime }${ number.to() }: ${ name.to() }"
}

private fun Operation.info2() : String = "${ type.to() }"

private fun Operation.info3() : String = "${ amount.to() } ${ unit.to() }"

private fun Any?.to() = this ?: "null"
