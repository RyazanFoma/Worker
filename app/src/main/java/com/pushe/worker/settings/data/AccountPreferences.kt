package com.pushe.worker.settings.data

data class AccountPreferences(
    var path: String = "",
    var account: String = "",
    var password: String = "",
    var swipeDown: Int = 0,
    var swipeRotation: Int = 0,
)
