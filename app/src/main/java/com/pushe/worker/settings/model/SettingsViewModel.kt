package com.pushe.worker.settings.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushe.worker.settings.data.AccountRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    var preferences = AccountRepository.getPreferences().value

    fun path(value: String) {
        this.viewModelScope.launch {
            dataStore.edit { it[AccountRepository.Keys.PATH] = value }
        }
    }

    fun account(value: String) {
        this.viewModelScope.launch {
            dataStore.edit { it[AccountRepository.Keys.ACCOUNT] = value }
        }
    }

    fun password(value: String) {
        this.viewModelScope.launch {
            dataStore.edit { it[AccountRepository.Keys.PASSWORD] = value }
        }
    }
}
