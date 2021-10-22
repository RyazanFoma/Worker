package com.pushe.worker.data

class LogUpDataSource(private val apiService: ERPRestService) {
    suspend fun load(barcode: String) = apiService.getUser(barcode = barcode)
}