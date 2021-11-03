package com.pushe.worker.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: AccountRepository
) : ViewModel() {
    var preferences: AccountPreferences? = null

    init {
        this.viewModelScope.launch {
            preferences = repository.accountPreferencesFlow.first()
        }
    }

    var path: String
        get() = preferences?.path ?: "null"
        set(value) { this.viewModelScope.launch { repository.updatePath(value) } }
    var account: String
        get() = preferences?.account ?: "null"
        set(value) { this.viewModelScope.launch { repository.updateAccount(value) } }
    var password: String
        get() = preferences?.password ?: "null"
        set(value) { this.viewModelScope.launch { repository.updatePassword(value) } }
}