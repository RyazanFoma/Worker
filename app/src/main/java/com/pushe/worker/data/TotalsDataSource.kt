package com.pushe.worker.data

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
