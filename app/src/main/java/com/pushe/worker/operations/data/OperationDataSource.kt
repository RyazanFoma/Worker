package com.pushe.worker.operations.data

import com.pushe.worker.utils.ERPRestHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OperationDataSource @Inject constructor() {

    @Inject
    lateinit var apiService: ERPRestHelper

    suspend fun load(userId: String, barcode: String) = apiService.getOperation(
        barcode = barcode,
        id = userId
    )

    suspend fun complete(userId: String, barcode: String) = apiService.postOperation(
        barcode = barcode,
        id = userId
    )

}