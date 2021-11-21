package com.pushe.worker.operations.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.pushe.worker.operations.data.Operation
import com.pushe.worker.utils.ErrorMessage
import kotlinx.coroutines.flow.Flow
import java.text.DecimalFormat
import kotlin.math.max

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
fun ListScreen(
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
        )
        operationsItems.apply {
            when {
                loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                    if (operationsItems.itemCount == 0)
                        Placeholder(
                            stickyColor = Color.Gray,
                            itemsColors = Color.Gray
                        )
                    else
                        CircularProgressIndicator(color = MaterialTheme.colors.secondary)
                }
                loadState.isError() -> loadState.errorMessage()?.let {
                    ErrorMessage(
                        error = it,
                        onRefresh = { operationsItems.refresh() }
                    )
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@Composable
private fun OperationList(
    items: LazyPagingItems<Operation>,
    isRefreshing: Boolean,
    stickyColor: Color,
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
                    item { OperationItem(operation = it) }
                }
            }
        }
    }
}

@Composable
private fun OperationHeader(modifier: Modifier = Modifier, date: String?, stickyColor: Color) {
    Card(modifier = modifier.fillMaxWidth(),
        backgroundColor = stickyColor) {
        Text(modifier = Modifier.padding(8.dp),
            text = date.convertDate())
    }
}

@ExperimentalMaterialApi
@Composable
private fun OperationItem(modifier: Modifier = Modifier, operation: Operation) {
    ListItem(
        modifier = modifier.padding(bottom = 8.dp),
        icon = {
            Icon(
                imageVector = Icons.Rounded.TaskAlt,
                contentDescription = "Выполнено",
                modifier = Modifier.size(48.dp)
            )
        },
        overlineText = { Text(text = operation.info1()) },
        text = { Text(text = operation.info2()) },
        secondaryText = { Text(text = operation.info3()) },
        singleLineSecondaryText = false,
        trailing = {
            Icon(
                Icons.Default.AccountBalanceWallet,
                contentDescription = "Wallet",
                tint = operation.sum?.let { Color.Unspecified }
                    ?: MaterialTheme.colors.onSurface.copy(0.2f)
            )
        }
    )
    Divider()
}

@ExperimentalMaterialApi
@Composable
private fun Placeholder(itemCount: Int = 15, stickyColor: Color, itemsColors: Color) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        OperationHeader(
            stickyColor = stickyColor,
            modifier = Modifier
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = stickyColor.copy(alpha = 0.2f)
                ),
            date = "9999-99-99"
        )
        for (i in 1..max(itemCount, 15))
            OperationItem(
                modifier = Modifier
                    .placeholder(
                        visible = true,
                        highlight = PlaceholderHighlight.shimmer(),
                        color = itemsColors.copy(alpha = 0.2f)
                    ),
                operation = Operation()
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
    val time = date?.let { if (it.length > 15) it.substring(11, 16) else null }
    return "${ time.to() } - ${ number.to() }: ${ name.to() }"
}

private fun Operation.info2() : String = "${ type.to() }"

private fun Operation.info3() : String {
    var res = "${ amount.to() } ${ unit.to() }"
    if (this.sum != null) res += " * ${ tariff.money() }/${ unit.to() } = ${ sum.money() }"
    return res
}

private fun Float?.money() = DecimalFormat("#,###.00").format(this ?: 0f) + " ₽"

private fun Any?.to() = this ?: "null"

private fun CombinedLoadStates.isError() : Boolean = with(this) {
    refresh is LoadState.Error ||
            append is LoadState.Error ||
            prepend is LoadState.Error ||
            source.refresh is LoadState.Error ||
            source.append is LoadState.Error ||
            source.prepend is LoadState.Error
}

private fun CombinedLoadStates.errorMessage() : String? =
    (refresh as? LoadState.Error ?:
    append as? LoadState.Error ?:
    prepend as? LoadState.Error ?:
    source.refresh as? LoadState.Error ?:
    source.append as? LoadState.Error ?:
    source.prepend as? LoadState.Error) ?.error?.message

