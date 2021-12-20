package com.pushe.worker.operations.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushe.worker.operations.data.Operation
import com.pushe.worker.operations.data.OperationDataSource
import com.pushe.worker.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException

class OperationViewModel(private val operationDataSource: OperationDataSource): ViewModel() {

    var operation: Operation by mutableStateOf(Operation())
        private set

    var status: Status by mutableStateOf(Status.UNKNOWN)
        private set

    var error: String = ""
        set(value) {
            status = Status.ERROR
            field = value
        }

    var resultMessage: String = ""

    fun load(barCode: String?) {
        status = Status.LOADING
        viewModelScope.launch {
            try {
                val response = operationDataSource.load(barcode = barCode!!)
                if (response.isSuccessful) {
                    operation = response.body()!!
                    status = Status.SUCCESS
                } else {
                    error = response.code().toString() + " - " + response.message() + "\n" +
                            (response.errorBody()?.stringSuspending() ?: "")
                }
            } catch (e: IOException) { // IOException for network failures.
                error = "IOException - " + e.localizedMessage
            } catch (e: HttpException) { // HttpException for any non-2xx HTTP status codes.
                error = "HttpException - " + e.localizedMessage
            }
        }
    }

    fun completed(barCode: String?) {
        status = Status.LOADING
        viewModelScope.launch {
            try {
                val response = operationDataSource.complete(barcode = barCode!!)
                if (response.isSuccessful) {
                    resultMessage = response.body()!!
                    status = Status.SUCCESS
                } else {
                    error = response.code().toString() + " - " + response.message() + "\n" +
                            (response.errorBody()?.stringSuspending() ?: "")
                }
            } catch (e: IOException) { // IOException for network failures.
                error = "IOException - " + e.localizedMessage
            } catch (e: HttpException) { // HttpException for any non-2xx HTTP status codes.
                error = "HttpException - " + e.localizedMessage
            }
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun ResponseBody.stringSuspending() = withContext(Dispatchers.IO) { string() }
}