package com.pushe.worker.operations.data

import com.pushe.worker.logup.model.LogUpViewModel
import com.pushe.worker.utils.ERPRestHelper
import com.pushe.worker.utils.ERPRestService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OperationDataSource @Inject constructor() {

    @Inject
    lateinit var apiService: ERPRestHelper
    @Inject
    lateinit var logUpViewModel: LogUpViewModel

    suspend fun load(barcode: String) = apiService.getOperation(
        barcode = barcode,
        id = logUpViewModel.userId
    )

    suspend fun complete(barcode: String) = apiService.postOperation(
        barcode = barcode,
        id = logUpViewModel.userId
    )

}