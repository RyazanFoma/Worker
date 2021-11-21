package com.pushe.worker.operations.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushe.worker.operations.data.Operation
import com.pushe.worker.utils.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//interface OperationViewModel1: ViewModel() {
//
//    /**
//     * by mutableStateOf(Operation())
//     */
//    var operation: Operation?
//
//    /**
//     * Default value is by mutableStateOf(Status.UNKNOWN)
//     */
//    var status: MutableState<Status>
//
//    /**
//     * Error message during ejection Status
//     */
//    var error: String?
//
//    /**
//     * Load an operation by its barcode
//     * @param barCode - barcode operation
//     */
//    fun load(barCode: String): Unit
//
//    /**
//     * Operation completion mark - completed
//     * @param number - number operation
//     * @param userId - the worker who completed this operation
//     */
//    fun completed(number: String, userId: String): Unit
//}

class OperationViewModel() : ViewModel() {

    var operation: Operation by mutableStateOf(Operation())
        private set

    var status: Status by mutableStateOf(Status.UNKNOWN)
        private set

    var error: String = ""
        set(value) {
            status = Status.ERROR
            field = value
        }

    fun load(barCode: String?) {
        status = Status.LOADING
        viewModelScope.launch {
            delay(10000)
            operation = Operation(
                number = "007",
                name = "Раскрой",
                type = "Раскрой чехла дивана 140",
                amount = 1f,
                unit = "шт",
            )
            status = Status.SUCCESS
        }
    }

    fun completed(number: String, userId: String) {
        status = Status.LOADING
        viewModelScope.launch {
            delay(10000)
            operation = Operation(
                number = "007",
                name = "Раскрой",
                type = "Раскрой чехла дивана 140",
                amount = 1f,
                unit = "шт",
                date = "2021-11-18T15:38:41",
                worker = userId,
            )
            status = Status.SUCCESS
        }
    }
}