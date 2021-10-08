package com.pushe.worker.data

import com.pushe.worker.data.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

/**
 * REST API to Retrofit to get a the ERP data
 */

interface ERPRestService {
    @Headers("Accept: application/json")
    @GET("user")
    fun getUser(@Query("id") id: String?): Call<LoggedInUser?>?

    @Headers("Accept: application/json")
    @GET("operations")
    suspend fun getOperations(@Query("id") userId: String,
                      @Query("date") dateOperations: String,
                      @Query("skip") skip: Int,
                      @Query("top") top: Int
    ): Operations<Operation>

    @Headers("Accept: application/json")
    @GET("operation")
    fun getOperation(@Query("barcode") barcode: String?): Call<Operation?>?

    @Headers("Accept: application/json")
    @GET("totals")
    suspend fun getTotals(
        @Query("id") id: String?,  // user id
        @Query("start") startDay: String?,  // first day of the request period
        @Query("end") endDay: String?,  // last day of the request period
        @Query("analytics") analyticsData: String? //maybe: type (вид), day (день), month (месяц)
    ): Response<List<Total>> //
}