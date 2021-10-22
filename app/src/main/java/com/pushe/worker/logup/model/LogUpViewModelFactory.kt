package com.pushe.worker.logup.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pushe.worker.data.ERPRestService
import com.pushe.worker.data.LogUpDataSource
import com.pushe.worker.utils.RetrofitClient

class LogUpViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val backend: ERPRestService
        get() = RetrofitClient.getClient(context).create(ERPRestService::class.java)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            return LogUpViewModel( logUpDataSource = LogUpDataSource(backend) ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}