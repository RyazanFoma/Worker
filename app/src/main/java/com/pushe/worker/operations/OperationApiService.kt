package com.pushe.worker.operations

import com.pushe.worker.operations.model.Operation
import com.pushe.worker.operations.model.Operations
import retrofit2.http.GET
import retrofit2.http.Query

interface OperationApiService {
    @GET("operations")
    suspend fun getOperations(@Query("id") userId: String,
                      @Query("date") dateOperations: String,
                      @Query("skip") skip: Int,
                      @Query("top") top: Int
    ): Operations<Operation>
}