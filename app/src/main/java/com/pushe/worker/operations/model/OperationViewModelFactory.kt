package com.pushe.worker.operations.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pushe.worker.operations.data.OperationDataSource
import com.pushe.worker.utils.ERPRestService
import com.pushe.worker.utils.RetrofitClient

class OperationViewModelFactory(
    private val userId: String
) : ViewModelProvider.Factory {
    private val apiService: ERPRestService
        get() {
            return RetrofitClient.getClient().create(ERPRestService::class.java)
        }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OperationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            return OperationViewModel(operationDataSource = OperationDataSource(apiService, userId)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}