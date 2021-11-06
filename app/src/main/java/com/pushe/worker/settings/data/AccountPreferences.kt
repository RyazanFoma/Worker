package com.pushe.worker.settings.data

data class AccountPreferences(
    val path: String = "http://10.0.2.2:8080",
    val account: String = "user",
    val password: String = "123456",
)
