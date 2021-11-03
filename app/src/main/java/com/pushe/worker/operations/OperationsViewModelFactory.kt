package com.pushe.worker.operations

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pushe.worker.data.ERPRestService
import com.pushe.worker.utils.RetrofitClient

class OperationsViewModelFactory(
    private val context: Context,
    private val userId: String
) : ViewModelProvider.Factory {
    private val apiService: ERPRestService
        get() {
            return RetrofitClient.getClient(context).create(ERPRestService::class.java)
        }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OperationsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            return OperationsViewModel(apiService = apiService, userId = userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}