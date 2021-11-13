package com.pushe.worker.logup.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushe.worker.logup.data.LogUpDataSource
import com.pushe.worker.utils.Status
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LogUpViewModel(private val logUpDataSource: LogUpDataSource? = null) : ViewModel() {

    var userId = ""
        private set
    var userName by mutableStateOf("")
    private var hashPassword = charArrayOf()

    /**
     * Message of load error
     */
    var error : String = ""
        set(value) {
            status = Status.ERROR
            field = value
        }

    var status: Status by mutableStateOf(Status.UNKNOWN)
        private set

    fun load(barcode: String) {
        if (logUpDataSource == null) return
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
                }
            } catch (e: IOException) { // IOException for network failures.
                error = e.message ?: "IOException" // TODO: describe more detail of error
            } catch (e: HttpException) { // HttpException for any non-2xx HTTP status codes.
                error = e.message ?: "HttpException" // TODO: describe more detail of error
            }
        }
    }

//    private fun Char.hash(i: Int) = (this.code * 13 - (i + 1) * 7 ) % 873 TODO: here open

    fun isVerified(password: String) : Boolean {
        var verified = true // presumption of innocence
        if (hashPassword.size != password.length) return false
        password.toCharArray().forEachIndexed { i, c ->
//            verified = verified && hashPassword[i].code == c.hash(i) TODO: here open below delete
            verified = verified && hashPassword[i].code == c.code
        }
        return verified
    }
}