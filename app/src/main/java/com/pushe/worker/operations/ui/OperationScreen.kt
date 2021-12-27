package com.pushe.worker.operations.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.DoneAll
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import com.pushe.worker.operations.data.Operation
import com.pushe.worker.utils.BarCode
import com.pushe.worker.utils.Chip
import com.pushe.worker.utils.Status

@ExperimentalMaterialApi
@Composable
fun OperationScreen(
    barCode: String?,
    status: Status,
    operation: Operation,
    onCompleted: () -> Unit,
    onBack: () -> Unit,
    ) {
    val backgroundColor = MaterialTheme.colors.primaryVariant

    Column(
        modifier = Modifier
            .widthIn(max = 500.dp)
            .heightIn(max = 400.dp)
            .background(backgroundColor)
    ) {
        Heading(barCode = barCode, onBack = onBack)
        Divider()
        Middle(
            status = status,
            operation = operation,
            backgroundColor = backgroundColor,
        )
        Divider()
        Footer(
            status = status,
            operation = operation,
            onCompleted = onCompleted,
            onBack = onBack,
        )
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun Preview() {
    OperationScreen(
        barCode = "C764",
        status = Status.SUCCESS,
        operation = Operation(
            number = "007",
            name = "Обивка",
            type = "Обивка дивана Пуше 160",
            amount = 1f,
            performed = 0f,
            unit = "шт",
        ),
        onCompleted = {}
    ) {}
}


@Composable
private fun Heading(
    modifier: Modifier = Modifier,
    barCode: String?,
    onBack: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FloatingActionButton(
            modifier = modifier.padding(8.dp),
            onClick = onBack
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = null)
        }
        barCode?.let {
            Surface(modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(8.dp),
                shape = RoundedCornerShape(10),
                color = Color.LightGray
            ) {
                BarCode(barCode = barCode)
            }
        }
    }
}

//@Preview
//@Composable
//fun Preview1() {
//    Heading(barCode = "C764", onBack = {})
//}

@ExperimentalMaterialApi
@Composable
private fun Middle(
    modifier: Modifier = Modifier,
    status: Status,
    operation: Operation,
    backgroundColor: Color
) {
    val color = MaterialTheme.colors.contentColorFor(backgroundColor)

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Center
    ) {
        ListItem(
            modifier = modifier
                .placeholder(
                    visible = operation.number == null,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = Color.LightGray.copy(alpha = 0.2f)
                )
                .padding(8.dp),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.TaskAlt,
                    contentDescription = "Выполнено",
                    modifier = modifier.size(48.dp),
                    tint = color
                )
            },
            overlineText = {
                Text(
                    modifier = modifier,
                    text = operation.info1(),
                    color = color
                )
                           },
            text = {
                Text(
                    modifier = modifier,
                    text = operation.info2(),
                    color = color
                )
                   },
            secondaryText = {
                Text(
                    modifier = modifier,
                    text = operation.info3(),
                    color = color
                )
                            },
            singleLineSecondaryText = false,
        )
        if (status == Status.LOADING) {
            CircularProgressIndicator(
                modifier = modifier.size(50.dp),
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

//@ExperimentalMaterialApi
//@Preview
//@Composable
//fun Preview2() {
//    Middle(
//        status = Status.LOADING,
//        operation = Operation(
//            number = "007",
//            name = "Обивка",
//            type = "Обивка дивана Пуше 160",
//            amount = 1f,
//            performed = 0f,
//            unit = "шт",
//        ),
//        backgroundColor = Color.Blue
//    )
//}

@Composable
private fun Footer(
    modifier: Modifier = Modifier,
    status: Status,
    operation: Operation,
    onCompleted: () -> Unit,
    onBack: () -> Unit, //TODO: Exit by timeout
) {
    var isDone by rememberSaveable { mutableStateOf(false)}
    var isComplete by rememberSaveable { mutableStateOf(false)}

    if (operation.amount != null && operation.performed != null) {
        isDone = operation.amount <= operation.performed
    }
    isComplete = !isDone && status == Status.SUCCESS
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
    ) {
        Row(
            modifier = modifier.padding(8.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Chip(
                modifier = modifier,
                text = "Всё",
                selected = isDone,
                color = MaterialTheme.colors.error,
            ) {
                Icon(
                    modifier = it,
                    imageVector = Icons.Outlined.DoneAll,
                    contentDescription = null
                )
            }
            Chip(
                modifier = modifier.padding(start = 8.dp),
                text = operation.info4(),
                selected = isComplete,
                color = MaterialTheme.colors.error,
            ) {
                Icon(
                    modifier = it,
                    imageVector = Icons.Default.Construction,
                    contentDescription = null
                )
            }
        }
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            ExtendedFloatingActionButton(
                modifier = modifier.padding(8.dp),
                backgroundColor = if (isComplete)
                    MaterialTheme.colors.secondary
                else
                    Color.LightGray,
                contentColor = MaterialTheme.colors.onSecondary,
                icon = { Icon(Icons.Filled.Done, contentDescription = "Работа выполнена") },
                text = { Text("ВЫПОЛНЕНО") },
                onClick = {
                    if (isComplete) {
                        isDone = true
                        operation.number?.let {
                            onCompleted()
                        }
                    }
                }
            )
        }
    }
}

//@Preview
//@Composable
//fun Preview3() {
//    Footer(
//        status = Status.SUCCESS,
//        userId = "C764",
//        worker = null,
//        number = "",
//        onCompleted = { _, _->}) {}
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

private fun Operation.info4() : String = "${ ((amount?:0f) - (performed?:0f)) } ${ unit.to() }"

private fun Any?.to() = this ?: ""
