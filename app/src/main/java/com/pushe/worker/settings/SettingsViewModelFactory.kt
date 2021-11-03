package com.pushe.worker.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SettingsViewModelFactory(private val dataStore: DataStore<Preferences>) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") // Guaranteed to succeed at this point.
            return SettingsViewModel(AccountRepository(dataStore = dataStore)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}