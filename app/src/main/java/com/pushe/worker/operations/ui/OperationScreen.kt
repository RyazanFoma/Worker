package com.pushe.worker.operations.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    val backgroundColor = MaterialTheme.colors.primaryVariant

    when (status) {
        Status.UNKNOWN ->
            barCode?.let(onRefresh)
        Status.ERROR ->
            ErrorMessage(
                error = error,
                onRefresh = { barCode?.let(onRefresh) }
            )
        else -> {}
    }
//    Box(
//        Modifier
//            .fillMaxSize()
//            .background(backgroundColor),
//        contentAlignment = Center
//    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 500.dp)
                .heightIn(max = 500.dp)
                .background(MaterialTheme.colors.primaryVariant)
        ) {
            Heading(onBack = onBack, barCode = barCode)
            Middle(operation = operation, status = status, backgroundColor = backgroundColor)
            Footer(
                userId = userId,
                operation = operation,
                onCompleted = onCompleted,
                onBack = onBack,
                status = status,
            )
        }
//    }
}

@Composable
private fun Heading(onBack: () -> Unit, barCode: String?) {
    Column(horizontalAlignment = Alignment.Start) {
        FloatingActionButton(
            modifier = Modifier.padding(8.dp),
            onClick = onBack
        ) { Icon(Icons.Filled.ArrowBack, contentDescription = null) }
        Divider()
        barCode?.let {
            Surface(modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(100.dp)
                .padding(8.dp)
                .align(CenterHorizontally),
                shape = RoundedCornerShape(10),
                color = Color.LightGray
            ) {
                BarCode(barCode = barCode)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun Middle(operation: Operation, status: Status, backgroundColor: Color) {
    val color = MaterialTheme.colors.contentColorFor(backgroundColor)

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Center
    ) {
        ListItem(
            modifier = Modifier.placeholder(
                visible = operation.number == null,
                highlight = PlaceholderHighlight.shimmer(),
                color = Color.LightGray.copy(alpha = 0.2f)
            ),
            icon = {
                Icon(
                    imageVector = Icons.Rounded.TaskAlt,
                    contentDescription = "Выполнено",
                    modifier = Modifier.size(48.dp),
                    tint = color
                )
            },
            overlineText = {
                Text(
                    text = operation.info1(),
                    color = color
                )
                           },
            text = {
                Text(
                    text = operation.info2(),
                    color = color
                )
                   },
            secondaryText = {
                Text(
                    text = operation.info3(),
                    color = color
                )
                            },
            singleLineSecondaryText = false,
        )
        if (status == Status.LOADING || status == Status.WRITING) {
            CircularProgressIndicator(
                Modifier
                    .size(50.dp),
                color = MaterialTheme.colors.secondary
            )
        }
    }
}

@Composable
private fun Footer(
    userId: String,
    operation: Operation,
    onCompleted: (number: String, userId: String) -> Unit,
    onBack: () -> Unit,
    status: Status,
) {
    val scope = rememberCoroutineScope()
    var isMy by rememberSaveable { mutableStateOf(false)}
    var isOther by rememberSaveable { mutableStateOf(false)}
    var isComplete by rememberSaveable { mutableStateOf(false)}

    isMy = isMy || userId == operation.worker
    isOther = operation.worker?.let { userId != it  } ?: false
    isComplete = !(isMy || isOther) && status == Status.SUCCESS

    Column(
        modifier = Modifier
            .padding(start = 8.dp, top = 8.dp, end = 8.dp)
            .fillMaxWidth(),
        horizontalAlignment = CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .align(End)
                .padding(bottom = 8.dp),
        ) {
            Chip(
                text = "Чужая",
                selected = isOther,
                color = MaterialTheme.colors.error,
            ) {
                Icon(
                    modifier = it,
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null
                )
            }
            Chip(
                modifier = Modifier.padding(start = 8.dp),
                text = "Моя",
                selected = isMy,
                color = MaterialTheme.colors.error,
            ) {
                Icon(
                    modifier = it,
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            }
        }
        Divider()
        ExtendedFloatingActionButton(
            modifier = Modifier.padding(8.dp),
            backgroundColor = if (isComplete)
                MaterialTheme.colors.secondary
                else Color.LightGray,
            contentColor = MaterialTheme.colors.onSecondary,
            icon = { Icon(Icons.Filled.Done, contentDescription = "Работа выполнена") },
            text = { Text("ВЫПОЛНЕНО") },
            onClick = {
                if (isComplete) {
                    isMy = true
                    operation.number?.let {
                        onCompleted(it, userId)
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
