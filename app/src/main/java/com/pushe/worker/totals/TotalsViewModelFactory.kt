package com.pushe.worker.totals

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pushe.worker.data.ERPRestService
import com.pushe.worker.data.TotalsDataSource
import com.pushe.worker.utils.RetrofitClient

class TotalsViewModelFactory (
    private val context: Context,
    private val userId: String
) : ViewModelProvider.Factory {

    private val backend: ERPRestService
        get() {
            return RetrofitClient.getClient(context).create(ERPRestService::class.java)
        }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TotalsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            return TotalsViewModel(
                totalsDataSource = TotalsDataSource(backend, userId)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}