package com.pushe.worker.operations.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pushe.worker.utils.ERPRestService
import com.pushe.worker.operations.data.TotalsDataSource
import com.pushe.worker.utils.RetrofitClient

class TotalsViewModelFactory (
    private val userId: String
) : ViewModelProvider.Factory {

    private val backend: ERPRestService
        get() = RetrofitClient.getClient().create(ERPRestService::class.java)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TotalsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            return TotalsViewModel( totalsDataSource = TotalsDataSource(backend, userId) ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}