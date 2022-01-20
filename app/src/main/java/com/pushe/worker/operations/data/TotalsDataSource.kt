package com.pushe.worker.operations.data

import com.pushe.worker.logup.model.LogUpViewModel
import com.pushe.worker.utils.ERPRestHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TotalsDataSource @Inject constructor(
) {

    @Inject
    lateinit var apiService: ERPRestHelper
    @Inject
    lateinit var logUpViewModel: LogUpViewModel

    suspend fun load(startDay: String, endDay: String, analytics: String) =
        apiService.getTotals(
            id = logUpViewModel.userId,
            startDay = startDay, endDay = endDay,
            analyticsData = analytics
        )

}
