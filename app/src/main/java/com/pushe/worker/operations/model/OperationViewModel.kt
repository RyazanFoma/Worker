package com.pushe.worker.operations.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushe.worker.operations.data.Operation
import com.pushe.worker.operations.data.OperationDataSource
import com.pushe.worker.utils.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

interface InterfaceOperationViewModel  {

    /**
     * var operation: Operation by mutableStateOf(Operation())
     *  privat set
     */
    val operation: Operation

    /**
     * var status: Status by mutableStateOf(Status.UNKNOWN)
     *  privat set
     */
    val status: Status

    /**
     * var error: String = ""
     *  set(value) {
     *      status = Status.ERROR
     *      field = value
     *  }
     */
    var error: String

    /**
     * Load an operation by its barcode
     * @param barCode - barcode operation
     */
    fun load(barCode: String?)

    /**
     * Operation completion mark - completed
     * @param number - number operation
     * @param userId - the worker who completed this operation
     */
    fun completed(barCode: String?)
}

class OperationViewModel(private val operationDataSource: OperationDataSource) : InterfaceOperationViewModel, ViewModel() {

    override var operation: Operation by mutableStateOf(Operation())
        private set

    override var status: Status by mutableStateOf(Status.UNKNOWN)
        private set

    override var error: String = ""
        set(value) {
            status = Status.ERROR
            field = value
        }

    var resultMessage: String = ""

    override fun load(barCode: String?) {
        status = Status.LOADING
        viewModelScope.launch {
            delay(10000)
            try {
                val response = operationDataSource.load(barcode = barCode!!)
                if (response.isSuccessful) {
                    operation = response.body()!!
                    status = Status.SUCCESS
                } else {
                    error = response.code().toString() + " - " + response.message() + "\n" +
                            response.errorBody()
                }
            } catch (e: IOException) { // IOException for network failures.
                error = "IOException - " + e.localizedMessage
            } catch (e: HttpException) { // HttpException for any non-2xx HTTP status codes.
                error = "HttpException - " + e.localizedMessage
            }
            status = Status.SUCCESS
        }
    }

    override fun completed(barCode: String?) {
        status = Status.LOADING
        viewModelScope.launch {
            delay(10000)
            try {
                val response = operationDataSource.complete(barcode = barCode!!)
                if (response.isSuccessful) {
                    resultMessage = response.body()!!
                    status = Status.SUCCESS
                } else {
                    error = response.code().toString() + " - " + response.message() + "\n" +
                            response.errorBody()
                }
            } catch (e: IOException) { // IOException for network failures.
                error = "IOException - " + e.localizedMessage
            } catch (e: HttpException) { // HttpException for any non-2xx HTTP status codes.
                error = "HttpException - " + e.localizedMessage
            }
            status = Status.SUCCESS
            delay(1000)
        }
    }
}