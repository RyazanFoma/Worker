package com.pushe.worker.utils

import com.pushe.worker.logup.data.User
import com.pushe.worker.operations.data.Operation
import com.pushe.worker.operations.data.Total
import retrofit2.Response

interface ERPRestHelper {
    suspend fun getUser(barcode: String?): Response<User>
    suspend fun getOperations(userId: String, skip: Int, top: Int, orderby: String = "Выполнено desc"):
            List<Operation>
    suspend fun getOperation(barcode: String?, id: String?): Response<Operation>
    suspend fun postOperation(barcode: String?, id: String?): Response<String>
    suspend fun getTotals(id: String?, startDay: String?, endDay: String?, analyticsData: String?):
            Response<List<Total>>
}