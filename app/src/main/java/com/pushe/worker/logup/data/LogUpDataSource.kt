package com.pushe.worker.logup.data

import com.pushe.worker.utils.ERPRestService

class LogUpDataSource(private val apiService: ERPRestService) {
    suspend fun load(barcode: String) = apiService.getUser(barcode = barcode)
}