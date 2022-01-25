package com.pushe.worker.settings.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object Keys {
        val PATH = stringPreferencesKey("erp_path")
        val ACCOUNT = stringPreferencesKey("erp_user")
        val PASSWORD = stringPreferencesKey("erp_password")
        val SWIPEDOWN = stringPreferencesKey("swipe_down")
        val SWIPEROTATION = stringPreferencesKey("swipe_rotation")
    }

    suspend fun path(value: String) {
        dataStore.edit { it[Keys.PATH] = value }
        preferences.path = value
    }
    val path: String
        get() = preferences.path

    suspend fun account(value: String) {
        dataStore.edit { it[Keys.ACCOUNT] = value }
        preferences.account = value
    }
    val account: String
        get() = preferences.account

    suspend fun password(value: String) {
        dataStore.edit { it[Keys.PASSWORD] = value }
        preferences.password = value
    }
    val password: String
        get() = preferences.password

    suspend fun swipeDown(value: Int) {
        dataStore.edit { it[Keys.SWIPEDOWN] = value.toString() }
        preferences.swipeDown = value
    }
    val swipeDown: Int
        get() = preferences.swipeDown

    suspend fun swipeRotation(value: Int) {
        dataStore.edit { it[Keys.SWIPEROTATION] = value.toString() }
        preferences.swipeRotation = value
    }
    val swipeRotation: Int
        get() = preferences.swipeRotation

    private var preferences by mutableStateOf(AccountPreferences())

    init {
        var flowPreferences: Flow<AccountPreferences>? = null

        if (flowPreferences == null) {
            flowPreferences = dataStore.data
                .catch { exception ->
                    if (exception is IOException) {
                        emit(emptyPreferences())
                    } else {
                        throw exception
                    }
                }
                .map {
                    AccountPreferences(
                        path = it[Keys.PATH] ?: "https://erp1c.pushe.ru/ERP_Test/hs/production/",
                        account = it[Keys.ACCOUNT] ?: "администратор",
                        password = it[Keys.PASSWORD] ?: "654",
                        swipeDown = it[Keys.SWIPEDOWN]?.toInt() ?: 3,
                        swipeRotation = it[Keys.SWIPEROTATION]?.toInt() ?: 3,
                    )
                }
                .distinctUntilChanged()
        }
        runBlocking {
            flowPreferences.firstOrNull()?.let { preferences = it }
        }
    }

}

