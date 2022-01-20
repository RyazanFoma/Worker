package com.pushe.worker.logup.data

import com.pushe.worker.utils.ERPRestHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LogUpDataSource @Inject constructor() {

    @Inject
    lateinit var apiService: ERPRestHelper

    suspend fun load(barcode: String) = apiService.getUser(barcode = barcode)
}