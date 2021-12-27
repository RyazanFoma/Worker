package com.pushe.worker.settings.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.IOException

object AccountRepository {
    object Keys {
        val PATH = stringPreferencesKey("erp_path")
        val ACCOUNT = stringPreferencesKey("erp_user")
        val PASSWORD = stringPreferencesKey("erp_password")
        val SWIPEDOWN = stringPreferencesKey("swipe_down")
        val SWIPEROTATION = stringPreferencesKey("swipe_rotation")
    }

    private inline val Preferences.path: String
        get() = this[Keys.PATH] ?: ""

    private inline val Preferences.account: String
        get() = this[Keys.ACCOUNT] ?: ""

    private inline val Preferences.password: String
        get() = this[Keys.PASSWORD] ?: ""

    private inline val Preferences.swipeDown: Int
        get() = this[Keys.SWIPEDOWN]?.toInt() ?: 3

    private inline val Preferences.swipeRotation: Int
        get() = this[Keys.SWIPEROTATION]?.toInt() ?: 3

    private var flowPreferences: Flow<AccountPreferences>? = null

    private var dataStore: DataStore<Preferences>? = null

    fun initPreference(dataStore: DataStore<Preferences>) {
        if (this.dataStore == null) this.dataStore = dataStore
    }

    fun getPreferences() : MutableState<AccountPreferences> {
        val preferences = mutableStateOf(AccountPreferences())

        if (flowPreferences == null) {
            if (dataStore == null) throw IllegalStateException("Account repository not initialized")
            flowPreferences = dataStore!!.data
                .catch { exception ->
                    // dataStore.data throws an IOException when an error is encountered when reading data
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    AccountPreferences(
                        path = it.path,
                        account = it.account,
                        password = it.password,
                        swipeDown = it.swipeDown,
                        swipeRotation = it.swipeRotation,
                    )
                }
                .distinctUntilChanged()
        }
        runBlocking {
            flowPreferences!!.firstOrNull()?.let { preferences.value = it }
        }
        return preferences
    }
}

