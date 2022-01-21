package com.pushe.worker.operations.data

import com.pushe.worker.utils.ERPRestHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TotalsDataSource @Inject constructor(
) {

    @Inject
    lateinit var apiService: ERPRestHelper

    suspend fun load(userId: String, startDay: String, endDay: String, analytics: String) =
        apiService.getTotals(
            id = userId,
            startDay = startDay, endDay = endDay,
            analyticsData = analytics
        )

}
