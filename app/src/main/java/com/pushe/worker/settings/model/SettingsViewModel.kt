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

    private var swipeDown: Boolean = true

    fun showSwipeDown(): Boolean = if (swipeDown && repository.swipeDown > 0) {
        this.viewModelScope.launch { repository.swipeDown(repository.swipeDown - 1) }
        swipeDown = false
        true
    }
    else false

    private var swipeRotation: Boolean = true

    fun showSwipeRotation(): Boolean = if (swipeRotation && repository.swipeRotation > 0) {
        this.viewModelScope.launch { repository.swipeRotation(repository.swipeRotation - 1) }
        swipeRotation = false
        true
    }
    else false

}
