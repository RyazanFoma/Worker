package com.pushe.worker.settings.data

data class AccountPreferences(
    val path: String = "",
    val account: String = "",
    val password: String = "",
    val swipeDown: Int = 3,
    val swipeRotation: Int = 3,
)
