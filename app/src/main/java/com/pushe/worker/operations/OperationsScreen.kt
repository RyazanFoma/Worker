package com.pushe.worker.operations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pushe.worker.data.model.Operation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@ExperimentalFoundationApi
@Composable
fun OperationsScreen(
    operationsFlow: Flow<PagingData<Operation>>,
    isRefreshing: Boolean
) {
    val operationsItems: LazyPagingItems<Operation> = operationsFlow.collectAsLazyPagingItems()

    operationsItems.apply {
        when {
            loadState.refresh is LoadState.Loading -> {
                println("First load")
            }
            loadState.append is LoadState.Loading -> {
                println("Retry Load")
            }
            loadState.append is LoadState.Error -> {
                println("Load error")
            }
        }
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { operationsItems.refresh() },
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            var lastData: String? = null

            for (i in 0 until operationsItems.itemCount) {
                operationsItems.peek(i)?.let {
                    val date = it.date?.substring(0, 10)
                    if (date != lastData) {
                        stickyHeader { OperationHeader(date = date) }
                        lastData = date
                    }
                    item { OperationCard(operation = it) }
                }
            }
        }
    }
}

@Composable
private fun OperationHeader(date: String?) {
    Card(modifier = Modifier.fillMaxWidth(),
        backgroundColor = MaterialTheme.colors.error) {
        Text(modifier = Modifier.padding(8.dp),
            text = date.convertDate())
    }
}

@Composable
private fun OperationCard(operation: Operation?) {
    val backgroundCard = if (operation?.sum != null)
        MaterialTheme.colors.surface
    else
        MaterialTheme.colors.primary

    Card(modifier = Modifier.fillMaxWidth(), backgroundColor = backgroundCard) {
        operation?.let {
            with(it) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Icon(Icons.Rounded.TaskAlt,
                        contentDescription = "Выполнено",
                        modifier = Modifier
                            .align(CenterVertically)
                            .padding(6.dp)
                            .size(48.dp)
                    )
                    Column {
                        Text(text = info1())
                        Text(text = info2())
                        Text(text = info3())
                    }
                }
            }
        }
    }
}

private fun String?.convertDate() : String {
    this?.let{if (it.length > 9) {
        return "${it.substring(8, 10)}.${it.substring(5, 7)}.${it.substring(0, 4)}"}
    }
    throw IllegalArgumentException("The input argument does not contain a date string yyyy-mm-dd")
}

private fun Operation.info1() : String {
    val time = date?.let { if (it.length > 15) it.substring(11, 16) else null }
    return "${ time.to() } - ${ number.to() }: ${ name.to() }"
}

private fun Operation.info2() : String = "${ type.to() }"

private fun Operation.info3() : String {
    var res = "${ amount.to() } ${ unit.to() }"
    if (this.sum != null) res += " * ${ tariff.to() }  ₽/${ unit.to() } = ${ sum.to() } ₽"
    return res
}

private fun Any?.to() = this ?: "null"
