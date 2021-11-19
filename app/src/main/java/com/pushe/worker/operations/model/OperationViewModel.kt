package com.pushe.worker.operations.model

import com.pushe.worker.operations.data.Operation
import com.pushe.worker.utils.Status

interface OperationViewModel {

    /**
     * by mutableStateOf(Operation())
     */
    var operation: Operation

    /**
     * Default value is by mutableStateOf(Status.UNKNOWN)
     */
    var status: Status

    /**
     * Load an operation by its barcode
     * @param barCode - barcode operation
     */
    fun load(barCode: String)

    /**
     * Operation completion mark - completed
     * @param number - number operation
     * @param userId - the worker who completed this operation
     */
    fun completed(number: String, userId: String)
}