package com.pushe.worker.operations

import com.pushe.worker.operations.model.Operation

sealed class OperationListItem(val key: String = "", val title: String = "", val subtitle: String = "") {
    data class Item(val operation: Operation) : OperationListItem(
        key = operation.id.toString(),
        title = operation.name.toString(),
        subtitle = "Длительность: ${operation.duration} ч. Тариф: ${operation.rate} р.")
    data class Separator(private val letter: String) : OperationListItem(
        key = letter.first().toString().uppercase(),
        title = letter.uppercase())
}
