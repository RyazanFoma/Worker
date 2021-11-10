package com.pushe.worker.operations.data

import com.pushe.worker.utils.ERPRestService

class TotalsDataSource(
    private val apiService: ERPRestService,
    private val userId: String
    ) {
    suspend fun load(startDay: String, endDay: String, analytics: String) =
        apiService.getTotals(
            id = userId,
            startDay = startDay, endDay = endDay,
            analyticsData = analytics
        )
}
