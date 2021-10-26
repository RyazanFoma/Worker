package com.pushe.worker.logup.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushe.worker.data.LogUpDataSource
import com.pushe.worker.utils.Status
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LogUpViewModel(private val logUpDataSource: LogUpDataSource) : ViewModel() {

    private var userId = ""
    var userName by mutableStateOf("")
    private var hashPassword = charArrayOf()

    /**
     * Message of load error
     */
    var error : String = ""
        private set

    var status: Status by mutableStateOf(Status.UNKNOWN)
        private set

    fun load(barcode: String) {
        this.viewModelScope.launch {
            status = Status.LOADING
            try {
                val response = logUpDataSource.load(barcode = barcode)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        userId = body.id ?: "none"
                        userName = body.name ?: "none"
                        hashPassword = body.password?.toCharArray() ?: charArrayOf()
                    }
                    status = Status.SUCCESS
                }
                else {
                    error = response.message()
                    status = Status.ERROR
                }
            } catch (e: IOException) { // IOException for network failures.
            error = e.message ?: "IOException"
            status = Status.ERROR
            } catch (e: HttpException) { // HttpException for any non-2xx HTTP status codes.
                error = e.message ?: "HttpException"
                status = Status.ERROR
            }
        }
    }
}