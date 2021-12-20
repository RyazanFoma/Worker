package com.pushe.worker.operations.data

import com.pushe.worker.utils.ERPRestService


class OperationDataSource(
    private val apiService: ERPRestService,
    private val userId: String
    ) {
    suspend fun load(barcode: String) = apiService.getOperation(barcode = barcode, id = userId)
    suspend fun complete(barcode: String) = apiService.postOperation(barcode = barcode, id = userId)
}