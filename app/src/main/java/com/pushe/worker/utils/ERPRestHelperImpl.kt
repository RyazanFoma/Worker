package com.pushe.worker.utils

import com.pushe.worker.logup.data.User
import com.pushe.worker.operations.data.Operation
import com.pushe.worker.operations.data.Total
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ERPRestHelperImpl @Inject constructor(private val restService: ERPRestService) : ERPRestHelper {

    override suspend fun getUser(barcode: String?): Response<User> = restService.getUser(barcode)

    override suspend fun getOperations(
        userId: String,
        skip: Int,
        top: Int,
        orderby: String
    ): List<Operation> = restService.getOperations(userId, skip, top, orderby)

    override suspend fun getOperation(barcode: String?, id: String?): Response<Operation> =
        restService.getOperation(barcode, id)

    override suspend fun postOperation(barcode: String?, id: String?): Response<String> =
        restService.postOperation(barcode, id)

    override suspend fun getTotals(
        id: String?,
        startDay: String?,
        endDay: String?,
        analyticsData: String?
    ): Response<List<Total>> = restService.getTotals(id, startDay, endDay, analyticsData)
}