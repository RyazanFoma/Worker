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
    private var isShowSwipeDown = true
    private var isShowSwipeRotation = true

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

    fun showSwipeDown(): Boolean {
        if (isShowSwipeDown && preferences.swipeDown > 0) {
            this.viewModelScope.launch {
                dataStore.edit {
                    it[AccountRepository.Keys.SWIPEDOWN] =
                        (preferences.swipeDown - 1).toString()
                }
            }
            isShowSwipeDown = false
            return true
        }
        return false
    }

    fun showSwipeRotation(): Boolean {
        if (isShowSwipeRotation && preferences.swipeRotation > 0) {
            this.viewModelScope.launch {
                dataStore.edit {
                    it[AccountRepository.Keys.SWIPEROTATION] =
                        (preferences.swipeRotation - 1).toString()
                }
            }
            isShowSwipeRotation = false
            return true
        }
        return false
    }
}
