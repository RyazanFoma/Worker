package com.pushe.worker.settings.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pushe.worker.settings.data.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    @Inject lateinit var repository: AccountRepository

    fun path(value: String) {
        this.viewModelScope.launch {
            repository.path(value)
        }
    }

    fun account(value: String) {
        this.viewModelScope.launch {
            repository.account(value)
        }
    }

    fun password(value: String) {
        this.viewModelScope.launch {
            repository.password(value)
        }
    }

    fun showSwipeDown(): Boolean = if (repository.swipeDown > 0) {
        this.viewModelScope.launch { repository.swipeDown(repository.swipeDown - 1) }
        true
    }
    else false

    fun showSwipeRotation(): Boolean = if (repository.swipeRotation > 0) {
        this.viewModelScope.launch { repository.swipeRotation(repository.swipeRotation - 1) }
        true
    }
    else false

}
