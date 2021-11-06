package com.pushe.worker.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.IOException

class AccountRepository(
    private val dataStore: DataStore<Preferences>,
) {
    private object Keys {
        val PATH = stringPreferencesKey("erp_path")
        val ACCOUNT = stringPreferencesKey("erp_user")
        val PASSWORD = stringPreferencesKey("erp_password")
    }

    private inline val Preferences.path
        get() = this[Keys.PATH] ?: ""

    private inline val Preferences.account
        get() = this[Keys.ACCOUNT] ?: ""

    private inline val Preferences.password
        get() = this[Keys.PASSWORD] ?: ""

    val preferencesFlow: Flow<AccountPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            AccountPreferences(
                path = preferences.path,
                account = preferences.account,
                password = preferences.password
            )
        }
        .distinctUntilChanged()

    suspend fun updatePath(path: String) {
        dataStore.edit { it[Keys.PATH] = path }
    }

   suspend fun updateAccount(account: String) {
        dataStore.edit { it[Keys.ACCOUNT] = account }
    }

    suspend fun updatePassword(password: String) {
        dataStore.edit { it[Keys.PASSWORD] = password }
    }
}

