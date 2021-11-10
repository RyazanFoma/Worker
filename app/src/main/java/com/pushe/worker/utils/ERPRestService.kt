package com.pushe.worker.utils

import com.pushe.worker.logup.data.User
import com.pushe.worker.operations.data.Operation
import com.pushe.worker.operations.data.Total
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * REST API to Retrofit to get and post a the ERP data
 */

interface ERPRestService {
    @Headers("Accept: application/json")
    @GET("user")
    suspend fun getUser(@Query("id") barcode: String?): Response<User>

    @Headers("Accept: application/json")
    @GET("operations")
    suspend fun getOperations(@Query("id") userId: String,
                      @Query("skip") skip: Int,
                      @Query("top") top: Int,
                      @Query("orderby") orderby: String = "Выполнено desc"
    ): List<Operation>

    @Headers("Accept: application/json")
    @GET("operation")
    fun getOperation(@Query("barcode") barcode: String?): Call<Operation?>?

    @Headers("Accept: application/json")
    @POST("operation")
    fun postOperation(@Query("id") id: String?): Call<Operation?>?

    @Headers("Accept: application/json")
    @GET("totals")
    suspend fun getTotals(
        @Query("id") id: String?,  // user id
        @Query("start") startDay: String?,  // first day of the request period
        @Query("end") endDay: String?,  // last day of the request period
        @Query("analytics") analyticsData: String? //maybe: type (вид), day (день), month (месяц)
    ): Response<List<Total>>
}