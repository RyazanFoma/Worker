package com.pushe.worker.operations

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pushe.worker.preference.RetrofitClient

/**
 * A [ViewModelProvider.Factory] that provides dependencies to [OperationViewModel],
 * allowing tests to switch out [OperationApiService] implementation via constructor injection.
 */
class OperationViewModelFactory(
    private val context: Context,
    private val userId: String,
    private val dateOperations: String
) : ViewModelProvider.Factory {

    private val backend: OperationApiService
        get() {
            return RetrofitClient.getClient(context).create(OperationApiService::class.java)
        }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OperationViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            return OperationViewModel(backend, userId, dateOperations) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}