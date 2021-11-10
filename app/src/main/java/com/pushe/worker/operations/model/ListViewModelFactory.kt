package com.pushe.worker.operations.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pushe.worker.utils.ERPRestService
import com.pushe.worker.utils.RetrofitClient

class ListViewModelFactory(
    private val context: Context,
    private val userId: String
) : ViewModelProvider.Factory {
    private val apiService: ERPRestService
        get() {
            return RetrofitClient.getClient(context).create(ERPRestService::class.java)
        }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            return ListViewModel(apiService = apiService, userId = userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}