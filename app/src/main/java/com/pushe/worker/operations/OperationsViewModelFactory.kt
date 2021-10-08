package com.pushe.worker.operations

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pushe.worker.data.ERPRestService
import com.pushe.worker.preference.RetrofitClient

/**
 * A [ViewModelProvider.Factory] that provides dependencies to [OperationsViewModel],
 * allowing tests to switch out [ERPRestService] implementation via constructor injection.
 */
class OperationsViewModelFactory(
    private val context: Context,
    private val userId: String,
    private val dateOperations: String
) : ViewModelProvider.Factory {

    private val backend: ERPRestService
        get() {
            return RetrofitClient.getClient(context).create(ERPRestService::class.java)
        }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OperationsViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            return OperationsViewModel(backend, userId, dateOperations) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}