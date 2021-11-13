package com.pushe.worker.logup.data

import com.pushe.worker.utils.ERPRestService
import retrofit2.Response

class LogUpDataSource(private val apiService: ERPRestService) {
    suspend fun load(barcode: String) = apiService.getUser(barcode = barcode)
}