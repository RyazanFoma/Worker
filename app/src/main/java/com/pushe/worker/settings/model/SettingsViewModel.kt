package com.pushe.worker.settings.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushe.worker.settings.data.AccountPreferences
import com.pushe.worker.settings.data.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: AccountRepository
) : ViewModel() {
    var flow: Flow<AccountPreferences> = repository.preferencesFlow

    fun path(value: String) {
        this.viewModelScope.launch { repository.updatePath(value) }
    }

    fun account(value: String) {
        this.viewModelScope.launch { repository.updateAccount(value) }
    }

    fun password(value: String) {
        this.viewModelScope.launch { repository.updatePassword(value) }
    }
}
