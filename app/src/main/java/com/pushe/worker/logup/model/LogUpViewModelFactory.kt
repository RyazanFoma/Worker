package com.pushe.worker.logup.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pushe.worker.utils.ERPRestService
import com.pushe.worker.logup.data.LogUpDataSource
import com.pushe.worker.utils.RetrofitClient

class LogUpViewModelFactory() : ViewModelProvider.Factory {
    private val backend: ERPRestService
        get() = RetrofitClient.getClient().create(ERPRestService::class.java)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogUpViewModel::class.java)) {
            var viewModel: LogUpViewModel
            try {
                viewModel = LogUpViewModel( logUpDataSource = LogUpDataSource(backend) )
            }
            catch (e: java.lang.IllegalArgumentException) {
                viewModel = LogUpViewModel()
                viewModel.error = e.message ?: "Неизвестная ошибка"
            }
            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            return viewModel as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}