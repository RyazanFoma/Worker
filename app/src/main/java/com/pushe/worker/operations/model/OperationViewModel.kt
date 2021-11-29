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

interface InterfaceOperationViewModel  {

    /**
     * var operation: Operation by mutableStateOf(Operation())
     *  privat set
     */
    val operation: Operation

    /**
     * var status: Status by mutableStateOf(Status.UNKNOWN)
     *  privat set
     */
    val status: Status

    /**
     * var error: String = ""
     *  set(value) {
     *      status = Status.ERROR
     *      field = value
     *  }
     */
    var error: String

    /**
     * Load an operation by its barcode
     * @param barCode - barcode operation
     */
    fun load(barCode: String?)

    /**
     * Operation completion mark - completed
     * @param number - number operation
     * @param userId - the worker who completed this operation
     */
    fun completed(number: String, userId: String)
}

class OperationViewModel : InterfaceOperationViewModel, ViewModel() {

    override var operation: Operation by mutableStateOf(Operation())
        private set

    override var status: Status by mutableStateOf(Status.UNKNOWN)
        private set

    override var error: String = ""
        set(value) {
            status = Status.ERROR
            field = value
        }

    override fun load(barCode: String?) {
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

    override fun completed(number: String, userId: String) {
        status = Status.LOADING
        viewModelScope.launch {
            delay(10000)
            operation = Operation(
                number = number,
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