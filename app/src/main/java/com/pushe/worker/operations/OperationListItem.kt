package com.pushe.worker.operations

import com.pushe.worker.operations.model.Operation
import java.util.*

sealed class OperationListItem(val key: String = "",
                               val title: String = "",
                               val subtitle: String? = null) {
    data class Item(val operation: Operation) : OperationListItem(
        key = operation.date.toString().substring(0, 9), //dd.mm.yyyy
        title = "${operation.date?.substring(11)}, ${operation.number}, ${operation.name}",
        subtitle = if (operation.tarrif != null && operation.sum!= null)
            "${operation.type} (${operation.tarrif} р.) - ${operation.amount} ${operation.unit}, Сумма: ${operation.sum} р."
        else
            "${operation.type} - ${operation.amount} ${operation.unit}")

    data class Separator(private val letter: String) : OperationListItem(
        key = letter,
        title = letter.toUpperCase(Locale.ROOT))
}
