package com.pushe.worker.logup.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushe.worker.logup.data.LogUpDataSource
import com.pushe.worker.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class LogUpViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var logUpDataSource: LogUpDataSource

    var userId = ""
        private set

    var userName by mutableStateOf("")
        private set

    private var hashPassword = charArrayOf()

    /**
     * Message of load error
     */
    var error : String = ""
        set(value) {
            status = Status.ERROR
            field = value
        }

    var status: Status by mutableStateOf(Status.LOADING)
        private set

    fun load(barcode: String) {
        this.viewModelScope.launch {
            status = Status.LOADING
            try {
                val response = logUpDataSource.load(barcode)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        userId = body.id ?: "none"
                        userName = body.name ?: "none"
                        hashPassword = body.password?.toCharArray() ?: charArrayOf()
                    }
                    status = Status.SUCCESS
                }
                else {
                    error = response.code().toString() + " - " + response.message() + "\n" +
                            response.errorBody()?.stringSuspending()
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

//    private fun Char.hash(i: Int) = (this.code * 13 - (i + 1) * 7 ) % 873 TODO: here open

    fun isVerified(password: String) : Boolean {
        var verified = true // presumption of innocence
        // TODO: here open
        if (/*password.length > 0 &&*/ hashPassword.size != password.length) return false
        password.toCharArray().forEachIndexed { i, c ->
//            verified = verified && hashPassword[i].code == c.hash(i) TODO: here open below delete
            verified = verified && hashPassword[i].code == c.code
        }
        return verified
    }
}