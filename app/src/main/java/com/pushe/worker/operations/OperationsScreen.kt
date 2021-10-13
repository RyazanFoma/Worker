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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pushe.worker.data.model.Operation
import kotlinx.coroutines.flow.Flow
import kotlin.math.max

@ExperimentalFoundationApi
@Composable
fun OperationsScreen(
    operationsFlow: Flow<PagingData<Operation>>,
    isRefreshing: Boolean
) {
    val operationsItems: LazyPagingItems<Operation> = operationsFlow.collectAsLazyPagingItems()

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {
        OperationList(
            items = operationsItems,
            isRefreshing = isRefreshing,
            stickyColor = MaterialTheme.colors.error,
            firstItemColor = MaterialTheme.colors.primary,
            lastItemColor = MaterialTheme.colors.surface
        )
        operationsItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    Placeholder(
                        itemCount = operationsItems.itemCount,
                        stickyColor = MaterialTheme.colors.error,
                        itemsColors = MaterialTheme.colors.primary
                    )
//                    println("First load ${operationsItems.itemCount}")
                }
                loadState.append is LoadState.Loading -> {
                    CircularProgressIndicator(color = MaterialTheme.colors.secondary)
//                    println("Retry Load ${operationsItems.itemCount}")
                }
                loadState.append is LoadState.Error ||
                loadState.refresh is LoadState.Error ||
                loadState.prepend is LoadState.Error ||
                loadState.source.prepend is LoadState.Error ||
                loadState.source.append is LoadState.Error ||
                loadState.source.refresh is LoadState.Error -> {
                    val errorState = loadState.source.refresh as? LoadState.Error
                        ?: loadState.source.append as? LoadState.Error
                        ?: loadState.source.prepend as? LoadState.Error
                        ?: loadState.refresh as? LoadState.Error
                        ?: loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error
                    errorState?.let {
//                        println("Load error: ${it.error.message}")
                        it.error.message?.let {
                                message -> ErrorMessage(
                                    error = message,
                                    onRefresh = { operationsItems.refresh() }
                                )
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun OperationList(
    items: LazyPagingItems<Operation>,
    isRefreshing: Boolean,
    stickyColor: Color,
    firstItemColor: Color,
    lastItemColor: Color
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { items.refresh() },
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            var lastData: String? = null

            for (i in 0 until items.itemCount) {
                items.peek(i)?.let {
                    val date = it.date?.substring(0, 10)
                    if (date != lastData) {
                        stickyHeader { OperationHeader(date = date, stickyColor = stickyColor) }
                        lastData = date
                    }
                    item {
                        OperationCard(
                            operation = it,
                            firstItemColor = firstItemColor,
                            lastItemColor = lastItemColor
                        )
                    }
                }
            }
        }
    }

}

@Composable
private fun OperationHeader(date: String?, stickyColor: Color) {
    Card(modifier = Modifier.fillMaxWidth(),
        backgroundColor = stickyColor) {
        Text(modifier = Modifier.padding(8.dp),
            text = date.convertDate())
    }
}

@Composable
private fun OperationCard(operation: Operation?, firstItemColor: Color, lastItemColor: Color) {
    val backgroundCard = if (operation?.sum != null)
        lastItemColor
    else
        firstItemColor

    Card(modifier = Modifier.fillMaxWidth(), backgroundColor = backgroundCard) {
        operation?.let {
            with(it) {
                Row(modifier = Modifier.padding(8.dp)) {
                    Icon(imageVector = Icons.Rounded.TaskAlt,
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

@Composable
private fun Placeholder(itemCount: Int = 15, stickyColor: Color, itemsColors: Color) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        CardPlaceholder(height = 32.dp, color = stickyColor)
        for (i in 1..max(itemCount, 15))
            CardPlaceholder(height = 78.dp, color = itemsColors)
    }
}

@Composable
private fun CardPlaceholder(height: Dp, color: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .placeholder(
                visible = true,
                highlight = PlaceholderHighlight.shimmer(),
                color = color.copy(alpha = 0.2f)
            )
    ) {}
}

@Composable
private fun ErrorMessage(error: String, onRefresh: () -> Unit) {
    Snackbar(
        modifier = Modifier.padding(50.dp),
        contentColor = MaterialTheme.colors.onError,
        backgroundColor = MaterialTheme.colors.error,
        action = {
            Button(onClick = { onRefresh() }) {
                Text(text = "Retry")
            }
        }
    ) {Text(text = error)}
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
