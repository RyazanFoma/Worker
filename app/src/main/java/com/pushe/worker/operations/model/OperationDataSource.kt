package com.pushe.worker.operations.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.pushe.worker.data.Result
import com.pushe.worker.data.ERPRestService
import com.pushe.worker.data.model.Operation
import com.pushe.worker.utils.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*


class OperationDataSource(val context: Context) : MutableLiveData<Result<*>>() {

    fun requestOperation(barcode: String?) {

        try {

          val call = RetrofitClient.getClient(context).create(UserApiService::class.java).getOperation(barcode)

            call.enqueue(object : Callback<Operation> {

                override fun onResponse(call: Call<Operation>,
                                        response: Response<Operation>) {
                    value = if (response.isSuccessful) {
                        Result.Success(response.body())
                    } else {
                        Result.Error(
                            RuntimeException(
                                String.format(
                                    Locale.US,
                                    "%d - %s",
                                    response.code(),
                                    response.message()
                                )
                            )
                        )
                    }
                }

                override fun onFailure(call: Call<Operation>, t: Throwable) {
                    value = Result.Error(RuntimeException(t.message))
                }
            })

        } catch (e: Exception) {
            value = Result.Error(IOException("Ошибка получения операции с сервера", e))
        }
    }

    fun observeAsState(): Any {

    }
}